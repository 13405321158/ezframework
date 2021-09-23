package com.leesky.ezframework.mybatis.result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class OneToManyResult<T, E> {
    private List<T> list;
    private String fieldCode;
    private String refColumn;
    private BaseMapper<E> mapperE;

    private Collection<E> CollectionAll;
    private FieldCollectionType fieldCollectionType;
    private List<Serializable> columnPropertyValueList;

    private Map<String, Boolean> isExeSqlMap;
    private Map<String, String> columnPropertyMap;
    private Map<String, String> refColumnPropertyMap;
    private Map<String, Collection<E>> collectionMap;

    public OneToManyResult(Field[] fields) {
        isExeSqlMap = Maps.newHashMap();
        collectionMap = Maps.newHashMap();
        for (Field f : fields) {
            isExeSqlMap.put(f.getName(), false);
            collectionMap.put(f.getName(), null);
        }
    }

    public void handle(Field field) {

        if (CollectionUtils.isNotEmpty(CollectionAll)) {
            for (T entity : list) {
                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                Collection<E> listForThisEntity = Lists.newArrayList();
                if (fieldCollectionType == FieldCollectionType.SET)
                    listForThisEntity = Sets.newHashSet();

                try {
                    for (E entityE : CollectionAll) {
                        Field entityField, entity2Field;

                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refComValue = entity2Field.get(entityE);

                        if (columnValue != null && refComValue != null && columnValue.toString().equals(refComValue.toString())) {
                            listForThisEntity.add(entityE);
                        }


                    }


                    if (listForThisEntity.size() > 0) {
                        field.set(entity, listForThisEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // end loop-entity
        } // end if

    }

    @SuppressWarnings("unchecked")
    public void handleLazy(Field field) {
        BaseMapper<E> mapper = this.mapperE;
        try {

            if (fieldCollectionType == FieldCollectionType.SET) {
                for (T entity : this.list) {

                    Set<E> setForThisEntityProxy = (Set<E>) Enhancer.create(Set.class, (LazyLoader) () -> {
                        Collection<E> listForThisEntity = common(field, mapper, entity);
                        return (Set<E>) listForThisEntity;
                    });

                    field.set(entity, setForThisEntityProxy);

                }
            }
            if (fieldCollectionType == FieldCollectionType.LIST) {

                for (T entity : this.list) {
                    List<E> listForThisEntityProxy = (List<E>) Enhancer.create(List.class, (LazyLoader) () -> {
                        Collection<E> listForThisEntity = common(field, mapper, entity);

                        return (List<E>) listForThisEntity;
                    });
                    field.set(entity, listForThisEntityProxy);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Collection<E> common(Field field, BaseMapper<E> mapper, T entity) throws NoSuchFieldException, IllegalAccessException {
        Collection<E> listForThisEntity = fieldCollectionType == FieldCollectionType.SET ? Sets.newHashSet() : Lists.newArrayList();

        if (!isExeSqlMap.get(field.getName())) {
            QueryWrapper<E> filter = new QueryWrapper<>();
            if (columnPropertyValueList.size() == 1)
                filter.eq(refColumn, columnPropertyValueList.get(0));
            else
                filter.in(refColumn, columnPropertyValueList);

            isExeSqlMap.put(field.getName(), true);
            collectionMap.put(field.getName(), mapper.selectList(filter));
        }

        List<E> listAll = (List<E>) collectionMap.get(field.getName());

        String columnProperty = columnPropertyMap.get(fieldCode);
        String refColumnProperty = refColumnPropertyMap.get(fieldCode);


        for (E entityE : listAll) {
            Field entityField, entity2Field;

            entityField = entity.getClass().getDeclaredField(columnProperty);
            entityField.setAccessible(true);
            Object columnValue = entityField.get(entity);

            entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
            entity2Field.setAccessible(true);
            Object refComValue = entity2Field.get(entityE);

            if (columnValue != null && refComValue != null && columnValue.toString().equals(refComValue.toString()))
                listForThisEntity.add(entityE);
        }

        return listForThisEntity;
    }


}
