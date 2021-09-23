package com.leesky.ezframework.mybatis.result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
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

@Data
@SuppressWarnings("unchecked")
public class ManyToOneResult<T, E> {
    private List<T> list;
    private Field[] fields;
    private List<E> CollectionAll;
    private boolean lazy;
    private Class<?> fieldClass;
    private String fieldCode;
    private String refColumn;
    private BaseMapper<E> mapperE;
    private FieldCollectionType fieldCollectionType;
    private List<Serializable> columnPropertyValueList;

    private Map<String, String> columnPropertyMap;
    private Map<String, String> refColumnPropertyMap;

    private FieldCondition<T> fc;
    private Map<String, Boolean> isExeSqlMap;
    private Map<String, Collection<E>> collectionMap;

    public ManyToOneResult(Field[] fields) {
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
                    String columnProperty = columnPropertyMap.get(fieldCode);
                    String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                    E objForThisEntity = null;

                    for (E entityE : CollectionAll) {
                        Field entityField, entity2Field;

                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refColumnValue = entity2Field.get(entityE);

                        if (columnValue != null && columnValue.toString().equals(refColumnValue.toString())) {
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
        final BaseMapper<E> mapper = this.mapperE;

        for (T entity : this.list) {

            Class<E> entityEClass = (Class<E>) field.getType();


            E objForThisEntityProxy = (E) Enhancer.create(entityEClass, (LazyLoader) () -> {
                if (!isExeSqlMap.get(field.getName())) {
                    if (columnPropertyValueList.size() == 1) {
                        collectionMap.put(field.getName(), mapper
                                .selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0))));
                    } else {
                        collectionMap.put(field.getName(),
                                mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList)));
                    }
                    isExeSqlMap.put(field.getName(), true);
                }

                List<E> listAll = (List<E>) collectionMap.get(field.getName());

                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                E objForThisEntity = null;

                for (E entityE : listAll) {
                    Field entityField, entity2Field;

                    entityField = entity.getClass().getDeclaredField(columnProperty);
                    entityField.setAccessible(true);
                    Object columnValue = entityField.get(entity);

                    entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                    entity2Field.setAccessible(true);
                    Object refColumnValue = entity2Field.get(entityE);

                    if (columnValue != null && refColumnValue != null
                            && columnValue.toString().equals(refColumnValue.toString())) {
                        objForThisEntity = entityE;
                        break;
                    }
                }

                if (listAll.size() == 0 || objForThisEntity == null) {
                    Class<E> e2Class = (Class<E>) fieldClass;
                    objForThisEntity = e2Class.getDeclaredConstructor().newInstance();
                }

                return objForThisEntity;
            });

            // 设置代理
            try {
                field.set(entity, objForThisEntityProxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
