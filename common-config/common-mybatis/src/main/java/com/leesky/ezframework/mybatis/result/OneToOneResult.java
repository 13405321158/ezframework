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
public class OneToOneResult<T, E> {
    private List<T> list;
    private Field[] fields;
    private Collection<E> CollectionAll;
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

    public OneToOneResult(Field[] fields) {
        isExeSqlMap = Maps.newHashMap();
        collectionMap = Maps.newHashMap();
        for (Field f : fields) {
            isExeSqlMap.put(f.getName(), false);
            collectionMap.put(f.getName(), null);
        }
    }

    public void handle(Field field) {
        List<E> listAll = null;
        if (!lazy)
            listAll = (List<E>) CollectionAll;


        if (CollectionUtils.isNotEmpty(listAll)) {
            for (int j = 0; j < list.size(); j++) {
                T entity = list.get(j);
                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);

                E objForThisEntity = null;

                for (int k = 0; k < listAll.size(); k++) {
                    E entityE = listAll.get(k);
                    Field entityField = null;
                    Field entity2Field = null;
                    try {
                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refCoumnValue = entity2Field.get(entityE);

                        if (columnValue != null && refCoumnValue != null
                                && columnValue.toString().equals(refCoumnValue.toString())) {
                            objForThisEntity = entityE;
                            break;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

                try {
                    field.set(entity, objForThisEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // end loop-entity
        } // end if

    }

    public void handleLazy(Field field) {
        final BaseMapper<E> mapper = (BaseMapper<E>) this.mapperE;

        for (int i = 0; i < this.list.size(); i++) {
            T entity = list.get(i);

            @SuppressWarnings("unchecked")
            Class<E> entityEClass = (Class<E>) field.getType();

            @SuppressWarnings("unchecked")
            E objForThisEntityProxy = (E) Enhancer.create(entityEClass, new LazyLoader() {
                @Override
                public E loadObject() throws Exception {
                    if (isExeSqlMap.get(field.getName()) == false) {
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

                    for (int k = 0; k < listAll.size(); k++) {
                        E entityE = listAll.get(k);
                        Field entityField = null;
                        Field entity2Field = null;
                        try {
                            entityField = entity.getClass().getDeclaredField(columnProperty);
                            entityField.setAccessible(true);
                            Object columnValue = entityField.get(entity);

                            entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
                            entity2Field.setAccessible(true);
                            Object refCoumnValue = entity2Field.get(entityE);

                            if (columnValue != null && refCoumnValue != null
                                    && columnValue.toString().equals(refCoumnValue.toString())) {
                                objForThisEntity = entityE;
                                break;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    if (listAll == null || listAll.size() == 0 || objForThisEntity == null) {
                        Class<E> e2Class = (Class<E>) fieldClass;
                        objForThisEntity = e2Class.getDeclaredConstructor().newInstance();
                    }

                    return (E) objForThisEntity;
                }

            });

            // 设置代理
            try {
                field.set(entity, objForThisEntityProxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static <E> List<E> getListResult(Field field) {
        return null;
    }

}
