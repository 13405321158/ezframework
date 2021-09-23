package com.leesky.ezframework.mybatis.result;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import com.leesky.ezframework.mybatis.mapper.CommonCode;
import com.leesky.ezframework.mybatis.utils.JoinTableUtil;

import lombok.Data;

@SuppressWarnings("unused")
@Data
public class ManyToManyResult<T, E, X> {
    private boolean lazy;
    private List<T> list;
    private Field[] fields;

    private String fieldCode;
    private String refColumn;
    private FieldCondition<T> fc;

    private BaseMapper<E> mapperE;
    private BaseMapper<X> mapperX;

    private String inverseRefColumn;
    private Collection<E> CollectionAll;

    private Map<String, List<X>> entityXListMap;
    private Map<String, String> columnPropertyMap;
    private final Map<String, Boolean> isExeSqlMap;

    private Map<String, String> refColumnPropertyMap;
    private Map<String, String> inverseColumnPropertyMap;
    private Map<String, String> inverseRefColumnPropertyMap;
    private final Map<String, Collection<E>> collectionMap;

    private FieldCollectionType fieldCollectionType;
    private List<Serializable> columnPropertyValueList;

    public void build01(boolean lazy, List<T> list, Field[] fields, BaseMapper<E> mapperE, BaseMapper<X> mapperX, String fieldCode, String refColumn, String inverseRefColumn,
                        FieldCollectionType fieldCollectionType, List<Serializable> columnPropertyValueList) {
        this.lazy = lazy;
        this.list = list;
        this.fields = fields;
        this.mapperE = mapperE;
        this.mapperX = mapperX;
        this.fieldCode = fieldCode;
        this.refColumn = refColumn;
        this.inverseRefColumn = inverseRefColumn;
        this.fieldCollectionType = fieldCollectionType;
        this.columnPropertyValueList = columnPropertyValueList;

    }

    public void build02(Map<String, String> columnPropertyMap, Map<String, String> refColumnPropertyMap, Map<String, String> inverseColumnPropertyMap,
                        Map<String, String> inverseRefColumnPropertyMap) {
        this.columnPropertyMap = columnPropertyMap;
        this.refColumnPropertyMap = refColumnPropertyMap;
        this.inverseColumnPropertyMap = inverseColumnPropertyMap;
        this.inverseRefColumnPropertyMap = inverseRefColumnPropertyMap;

    }

    public ManyToManyResult(Field[] fields) {
        isExeSqlMap = Maps.newHashMap();
        collectionMap = Maps.newHashMap();
        for (Field field : fields) {
            isExeSqlMap.put(field.getName(), false);
            collectionMap.put(field.getName(), null);
        }
    }

    public void handle(Field field) {

        if (CollectionUtils.isNotEmpty(CollectionAll)) {

            String columnProperty = columnPropertyMap.get(fieldCode);
            String refColumnProperty = refColumnPropertyMap.get(fieldCode);
            String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
            String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
            List<X> entityXList = entityXListMap.get(fieldCode);

            Collection<E> listForThisEntity = fieldCollectionType == FieldCollectionType.SET ? Sets.newHashSet() : Lists.newArrayList();
            try {
                for (T entity : list) {

                    for (E entityE : CollectionAll) {
                        Field entityField, entity2Field, entityXField, entityXField2;

                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(inverseColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refComValue = entity2Field.get(entityE);

                        for (X entityX : entityXList) {
                            entityXField = entityX.getClass().getDeclaredField(JoinTableUtil.getRefColumnProperty(refColumn));
                            entityXField.setAccessible(true);
                            Object columnValueX = entityXField.get(entityX);

                            entityXField2 = entityX.getClass().getDeclaredField(JoinTableUtil.getInverseRefColumnProperty(inverseRefColumn));
                            entityXField2.setAccessible(true);
                            Object refColumnValueX = entityXField2.get(entityX);

                            if (columnValueX != null && columnValue != null && refColumnValueX != null
                                    && refComValue != null && columnValueX.toString().equals(columnValue.toString())
                                    && refColumnValueX.toString().equals(refComValue.toString())) {
                                listForThisEntity.add(entityE);
                            }
                        }
                    }
                    field.set(entity, listForThisEntity);

                } // end loop-entity

            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end if

    }

    @SuppressWarnings("unchecked")
    public void handleLazy(Field field) throws IllegalAccessException {
        final BaseMapper<E> mapper = this.mapperE;
        Map<String, List<X>> entityXListMap = Maps.newHashMap();
        String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
        if (fieldCollectionType == FieldCollectionType.SET) {

            for (T entity : this.list) {

                Set<E> setForThisEntityProxy = (Set<E>) Enhancer.create(Set.class, (LazyLoader) () -> {
                    if (!isExeSqlMap.get(field.getName())) {

                        List<X> entityXList = getXes(field, entityXListMap);

                        List<Serializable> idList = CommonCode.getSerializable(inverseRefColumnProperty, entityXList);
                        QueryWrapper<E> filter = new QueryWrapper<>();
                        if (idList.size() == 1) {
                            filter.eq(inverseRefColumn, idList.get(0));
                            collectionMap.put(field.getName(), mapper.selectList(filter));
                        } else if (idList.size() > 1) {
                            filter.in(inverseRefColumn, idList);
                            collectionMap.put(field.getName(), mapper.selectList(filter));
                        }
                        isExeSqlMap.put(field.getName(), true);

                        if (idList.size() == 0)
                            return Sets.newHashSet();
                    }

                    return common(field, entityXListMap, entity);
                });

                field.set(entity, setForThisEntityProxy);

            }
        }
        if (fieldCollectionType == FieldCollectionType.LIST) {
            for (T entity : this.list) {

                List<E> listForThisEntityProxy = (List<E>) Enhancer.create(List.class, (LazyLoader) () -> {

                    if (!isExeSqlMap.get(field.getName())) {
                        List<X> entityXList = getXes(field, entityXListMap);

                        List<Serializable> idList = Lists.newArrayList();
                        List<Serializable> idListDistinct = Lists.newArrayList();

                        CommonCode.extracted(inverseRefColumnProperty, entityXList, idList);
                        CommonCode.buildList(idListDistinct, idList);

                        QueryWrapper<E> filter = new QueryWrapper<>();
                        if (idListDistinct.size() == 1) {
                            filter.eq(inverseRefColumn, idListDistinct.get(0));
                            collectionMap.put(field.getName(), mapper.selectList(filter));
                        } else if (idListDistinct.size() > 1) {
                            filter.in(inverseRefColumn, idListDistinct);
                            collectionMap.put(field.getName(), mapper.selectList(filter));
                        }

                        isExeSqlMap.put(field.getName(), true);

                        if (idListDistinct.size() == 0)
                            return Lists.newArrayList();

                    }
                    return common(field, entityXListMap, entity);
                });

                field.set(entity, listForThisEntityProxy);

            }
        }

    }

    private List<X> getXes(Field field, Map<String, List<X>> entityXListMap) {
        isExeSqlMap.put(field.getName(), true);
        QueryWrapper<X> filter = new QueryWrapper<X>();
        if (columnPropertyValueList.size() == 1)
            filter.eq(refColumn, columnPropertyValueList.get(0));
        else
            filter.in(refColumn, columnPropertyValueList);

        List<X> entityXList = mapperX.selectList(filter);
        if (!entityXListMap.containsKey(fieldCode))
            entityXListMap.put(fieldCode, entityXList);
        
        return entityXList;
    }

    private Collection<E> common(Field field, Map<String, List<X>> entityXListMap, T entity) throws NoSuchFieldException, IllegalAccessException {
        List<X> entityXList = entityXListMap.get(fieldCode);

        List<E> listAll = (List<E>) collectionMap.get(field.getName());

        String columnProperty1 = columnPropertyMap.get(fieldCode);
        String refColumnProperty1 = refColumnPropertyMap.get(fieldCode);
        String inverseColumnProperty1 = inverseColumnPropertyMap.get(fieldCode);
        String inverseRefColumnProperty1 = inverseRefColumnPropertyMap.get(fieldCode);

        Collection<E> listForThisEntity = fieldCollectionType == FieldCollectionType.SET ? Sets.newHashSet() : Lists.newArrayList();

        for (E entityE : listAll) {
            Field entityField, entity2Field, entityXField, entityXField2;

            entityField = entity.getClass().getDeclaredField(columnProperty1);
            entityField.setAccessible(true);
            Object columnValue = entityField.get(entity);

            entity2Field = entityE.getClass().getDeclaredField(inverseColumnProperty1);
            entity2Field.setAccessible(true);
            Object refColumnValue = entity2Field.get(entityE);

            for (X entityX : entityXList) {
                entityXField = entityX.getClass().getDeclaredField(JoinTableUtil.getRefColumnProperty(refColumn));
                entityXField.setAccessible(true);
                Object columnValueX = entityXField.get(entityX);

                entityXField2 = entityX.getClass().getDeclaredField(JoinTableUtil.getInverseRefColumnProperty(inverseRefColumn));
                entityXField2.setAccessible(true);
                Object refColumnValueX = entityXField2.get(entityX);

                if (columnValueX != null && columnValue != null && refColumnValueX != null && refColumnValue != null && columnValueX.toString().equals(columnValue.toString())
                        && refColumnValueX.toString().equals(refColumnValue.toString())) {
                    listForThisEntity.add(entityE);
                }
            }

        }

        return listForThisEntity;
    }

}
