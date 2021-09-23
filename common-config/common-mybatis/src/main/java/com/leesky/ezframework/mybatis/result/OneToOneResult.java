package com.leesky.ezframework.mybatis.result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import com.leesky.ezframework.mybatis.mapper.MapperUtil;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("unchecked")
public class OneToOneResult<T, E> {
    private List<T> list;
    private String fieldCode;
    private String refColumn;
    private Class<?> fieldClass;
    private List<E> CollectionAll;
    private BaseMapper<E> mapperE;

    private FieldCollectionType fieldCollectionType;
    private List<Serializable> columnPropertyValueList;

    private Map<String, String> columnPropertyMap;
    private Map<String, String> refColumnPropertyMap;


    private Map<String, Boolean> isExeSqlMap;
    private Map<String, Collection<E>> collectionMap;

    public OneToOneResult(Field[] fields) {
        isExeSqlMap = Maps.newHashMap();
        collectionMap = Maps.newHashMap();
        for (Field f : fields) {
            isExeSqlMap.put(f.getName(), false);
            collectionMap.put(f.getName(), null);
        }
    }

    public void handle(Field field) {
        try {

            if (CollectionUtils.isNotEmpty(CollectionAll)) {

                for (T entity : list) {
                    E objForThisEntity = null;
                    String columnProperty = columnPropertyMap.get(fieldCode);
                    String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                    for (E entityE : CollectionAll) {
                        Field entityField, entity2Field;

                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refComValue = entity2Field.get(entityE);

                        if (columnValue != null && refComValue != null && columnValue.toString().equals(refComValue.toString())) {
                            objForThisEntity = entityE;
                            break;
                        }

                    }

                    field.set(entity, objForThisEntity);

                } // end loop-entity

            } // end if
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLazy(Field field) {
        BaseMapper<E> mapper = this.mapperE;

        for (T entity : this.list) {
            try {
                Class<E> entityEClass = (Class<E>) field.getType();

                E objForThisEntityProxy = (E) Enhancer.create(entityEClass, (LazyLoader) () -> {

                    if (!isExeSqlMap.get(field.getName())) {

                        if (columnPropertyValueList.size() == 1)
                            collectionMap.put(field.getName(), mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0))));
                        else
                            collectionMap.put(field.getName(), mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList)));

                        isExeSqlMap.put(field.getName(), true);
                    }

                    List<E> listAll = (List<E>) collectionMap.get(field.getName());

                    String columnProperty = columnPropertyMap.get(fieldCode);
                    String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                    E objForThisEntity = null;

                    for (E entityE : listAll) {
                        Field entityField;
                        Field entity2Field;

                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refComValue = entity2Field.get(entityE);

                        if (columnValue != null && refComValue != null && columnValue.toString().equals(refComValue.toString())) {
                            objForThisEntity = entityE;
                            break;
                        }

                    }

                    if (CollectionUtils.isEmpty(listAll) || objForThisEntity == null) {
                        Class<E> e2Class = (Class<E>) fieldClass;
                        objForThisEntity = e2Class.getDeclaredConstructor().newInstance();
                    }

                    return objForThisEntity;
                });

                // 设置代理

                field.set(entity, objForThisEntityProxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void build01(List<T> list, String fieldCode, String refColumn, Class<?> fieldClass) {
        this.setList(list);
        this.setFieldCode(fieldCode);
        this.setRefColumn(refColumn);
        this.setFieldClass(fieldClass);
    }

    public void build02(BaseMapper<E> mapper, FieldCollectionType fieldCollectionType, List<Serializable> columnPropertyValueList, MapperUtil<T, E, ?> o2oMaps) {
        this.setMapperE(mapper);
        this.setFieldCollectionType(fieldCollectionType);
        this.setColumnPropertyValueList(columnPropertyValueList);

        this.setColumnPropertyMap(o2oMaps.columnPropertyMap);
        this.setRefColumnPropertyMap(o2oMaps.refColumnPropertyMap);
    }


}
