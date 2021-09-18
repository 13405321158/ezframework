package com.leesky.ezframework.mybatis.result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import com.leesky.ezframework.mybatis.mapper.AbstractAutoMapper;
import com.leesky.ezframework.mybatis.utils.JoinTableUtil;
import lombok.Data;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

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

    public void build01(boolean lazy, List<T> list, Field[] fields, BaseMapper<E> mapperE, BaseMapper<X> mapperX, String fieldCode,
                        String refColumn, String inverseRefColumn, FieldCollectionType fieldCollectionType, List<Serializable> columnPropertyValueList) {
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

    public void build02(Map<String, String> columnPropertyMap, Map<String, String> refColumnPropertyMap,
                        Map<String, String> inverseColumnPropertyMap, Map<String, String> inverseRefColumnPropertyMap) {
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
        List<E> listAll = null;
        if (!lazy) {
            if (fieldCollectionType == FieldCollectionType.SET) {
                Set<E> setAll = (Set<E>) CollectionAll;
                if (setAll != null) {
                    listAll = Lists.newArrayList(setAll);
                }
            } else {
                listAll = (List<E>) CollectionAll;
            }
        }

        if (listAll != null && listAll.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                T entity = list.get(j);
                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);
                String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
                String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
                List<X> entityXList = entityXListMap.get(fieldCode);

                Collection<E> listForThisEntity = new ArrayList<E>();
                if (fieldCollectionType == FieldCollectionType.SET) {
                    listForThisEntity = new HashSet<E>();
                }

                for (int k = 0; k < listAll.size(); k++) {
                    E entityE = listAll.get(k);
                    Field entityField, entity2Field, entityXField, entityXField2;
                    try {
                        entityField = entity.getClass().getDeclaredField(columnProperty);
                        entityField.setAccessible(true);
                        Object columnValue = entityField.get(entity);

                        entity2Field = entityE.getClass().getDeclaredField(inverseColumnProperty);
                        entity2Field.setAccessible(true);
                        Object refCoumnValue = entity2Field.get(entityE);

                        // table1~table3&&table2~table3

                        for (int x = 0; x < entityXList.size(); x++) {
                            X entityX = entityXList.get(x);

                            entityXField = entityX.getClass()
                                    .getDeclaredField(JoinTableUtil.getRefColumnProperty(refColumn));
                            entityXField.setAccessible(true);
                            Object columnValueX = entityXField.get(entityX);

                            entityXField2 = entityX.getClass()
                                    .getDeclaredField(JoinTableUtil.getInverseRefColumnProperty(inverseRefColumn));
                            entityXField2.setAccessible(true);
                            Object refColumnValueX = entityXField2.get(entityX);

                            if (columnValueX != null && columnValue != null && refColumnValueX != null
                                    && refCoumnValue != null && columnValueX.toString().equals(columnValue.toString())
                                    && refColumnValueX.toString().equals(refCoumnValue.toString())) {
                                listForThisEntity.add(entityE);
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

                try {
                    field.set(entity, listForThisEntity);
                } catch (

                        Exception e) {
                    e.printStackTrace();
                }
            } // end loop-entity
        } // end if

    }

    public void handleLazy(Field field) {
        final BaseMapper<E> mapper = (BaseMapper<E>) this.mapperE;
        Map<String, List<X>> entityXListMap = Maps.newHashMap();

        if (fieldCollectionType == FieldCollectionType.SET) {

            for (int i = 0; i < this.list.size(); i++) {
                T entity = list.get(i);

                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);
                String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
                String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
                // List<X> entityXList = entityXListMap.get(fieldCode);

                @SuppressWarnings("unchecked")
                Set<E> setForThisEntityProxy = (Set<E>) Enhancer.create(Set.class, new LazyLoader() {

                    @Override
                    public Set<E> loadObject() {

                        List<X> entityXList;
                        if (isExeSqlMap.get(field.getName()) == false) {
                            isExeSqlMap.put(field.getName(), true);

                            if (columnPropertyValueList.size() == 1) {
                                entityXList = mapperX
                                        .selectList(new QueryWrapper<X>().select("DISTINCT " + inverseRefColumn)
                                                .eq(refColumn, columnPropertyValueList.get(0)));
                            } else {
                                entityXList = mapperX.selectList(new QueryWrapper<X>()
                                        .select("DISTINCT " + inverseRefColumn).in(refColumn, columnPropertyValueList));
                            }

                            if (!entityXListMap.containsKey(fieldCode)) {
                                entityXListMap.put(fieldCode, entityXList);
                            }

                            List<Serializable> idList = Lists.newArrayList();
                            for (int ii = 0; ii < entityXList.size(); ii++) {
                                X entityX = entityXList.get(ii);
                                try {
                                    Field fieldX = entityX.getClass().getDeclaredField(inverseRefColumnProperty);
                                    fieldX.setAccessible(true);
                                    Serializable id = (Serializable) fieldX.get(entityX);
                                    if (id != null && !idList.contains(id)) {
                                        idList.add(id);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            }
                            List<Serializable> idListDistinct = Lists.newArrayList();
                            if (idList.size() > 0)
                                AbstractAutoMapper.buildList(idListDistinct, idList);

                            idList = idListDistinct;

                            if (idList.size() == 1) {
                                collectionMap.put(field.getName(),
                                        mapper.selectList(new QueryWrapper<E>().eq(inverseRefColumn, idList.get(0))));
                            } else if (idList.size() > 1) {
                                collectionMap.put(field.getName(), mapper.selectList(
                                        new QueryWrapper<E>().in(inverseRefColumn, (ArrayList<Serializable>) idList)));
                            }

                            isExeSqlMap.put(field.getName(), true);

                            if (idList.size() == 0) {
                                return new HashSet<E>();
                            }

                        }

                        entityXList = entityXListMap.get(fieldCode);

                        List<E> listAll = (List<E>) collectionMap.get(field.getName());

                        String columnProperty = columnPropertyMap.get(fieldCode);
                        String refColumnProperty = refColumnPropertyMap.get(fieldCode);
                        String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
                        String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
                        // List<X> entityXList = entityXListMap.get(fieldCode);

                        Collection<E> listForThisEntity = new ArrayList<E>();
                        if (fieldCollectionType == FieldCollectionType.SET) {
                            listForThisEntity = new HashSet<E>();
                        }

                        for (int k = 0; k < listAll.size(); k++) {
                            E entityE = listAll.get(k);
                            Field entityField, entity2Field, entityXField, entityXField2;
                            try {
                                entityField = entity.getClass().getDeclaredField(columnProperty);
                                entityField.setAccessible(true);
                                Object columnValue = entityField.get(entity);

                                entity2Field = entityE.getClass().getDeclaredField(inverseColumnProperty);
                                entity2Field.setAccessible(true);
                                Object refColumnValue = entity2Field.get(entityE);

                                // table1~table3&&table2~table3
                                for (int x = 0; x < entityXList.size(); x++) {
                                    X entityX = entityXList.get(x);

                                    entityXField = entityX.getClass()
                                            .getDeclaredField(JoinTableUtil.getRefColumnProperty(refColumn));
                                    entityXField.setAccessible(true);
                                    Object columnValueX = entityXField.get(entityX);

                                    entityXField2 = entityX.getClass().getDeclaredField(
                                            JoinTableUtil.getInverseRefColumnProperty(inverseRefColumn));
                                    entityXField2.setAccessible(true);
                                    Object refColumnValueX = entityXField2.get(entityX);

                                    if (columnValueX != null && columnValue != null && refColumnValueX != null
                                            && refColumnValue != null
                                            && columnValueX.toString().equals(columnValue.toString())
                                            && refColumnValueX.toString().equals(refColumnValue.toString())) {
                                        listForThisEntity.add(entityE);
                                    }
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }

                        return (Set<E>) listForThisEntity;
                    }

                });

                // 设置代理
                try {
                    field.set(entity, setForThisEntityProxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < this.list.size(); i++) {
                T entity = list.get(i);

                String columnProperty = columnPropertyMap.get(fieldCode);
                String refColumnProperty = refColumnPropertyMap.get(fieldCode);
                String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
                String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
                // List<X> entityXList = entityXListMap.get(fieldCode);

                @SuppressWarnings("unchecked")
                List<E> listForThisEntityProxy = (List<E>) Enhancer.create(List.class, new LazyLoader() {

                    @Override
                    public List<E> loadObject() {

                        List<X> entityXList = null;
                        if (isExeSqlMap.get(field.getName()) == false) {
                            isExeSqlMap.put(field.getName(), true);

                            if (columnPropertyValueList.size() == 1) {
                                entityXList = mapperX
                                        .selectList(new QueryWrapper<X>().select("DISTINCT " + inverseRefColumn)
                                                .eq(refColumn, columnPropertyValueList.get(0)));
                            } else {
                                entityXList = mapperX.selectList(new QueryWrapper<X>()
                                        .select("DISTINCT " + inverseRefColumn).in(refColumn, columnPropertyValueList));
                            }
                            if (!entityXListMap.containsKey(fieldCode)) {
                                entityXListMap.put(fieldCode, entityXList);
                            }

                            ArrayList<Serializable> idList = Lists.newArrayList();
                            for (int ii = 0; ii < entityXList.size(); ii++) {
                                X entityX = entityXList.get(ii);
                                try {
                                    Field fieldX = entityX.getClass().getDeclaredField(inverseRefColumnProperty);
                                    fieldX.setAccessible(true);
                                    Serializable id = (Serializable) fieldX.get(entityX);
                                    if (id != null && !idList.contains(id)) {
                                        idList.add(id);
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            }
                            ArrayList<Serializable> idListDistinct = Lists.newArrayList();

                            for (int s = 0; s < idList.size(); s++) {
                                boolean isExists = false;
                                for (int ss = 0; ss < idListDistinct.size(); ss++) {
                                    if (idList.get(s) != null && idListDistinct.get(ss) != null
                                            && idList.get(s).toString().equals(idListDistinct.get(ss).toString())) {
                                        isExists = true;
                                        break;
                                    }
                                }

                                if (idList.get(s) != null && !isExists) {
                                    idListDistinct.add(idList.get(s));
                                }
                            }

                            idList = idListDistinct;

                            if (idList.size() == 1) {
                                collectionMap.put(field.getName(),
                                        mapper.selectList(new QueryWrapper<E>().eq(inverseRefColumn, idList.get(0))));
                            } else if (idList.size() > 1) {
                                collectionMap.put(field.getName(), mapper.selectList(
                                        new QueryWrapper<E>().in(inverseRefColumn, (ArrayList<Serializable>) idList)));
                            }

                            isExeSqlMap.put(field.getName(), true);

                            if (idList.size() == 0) {
                                return new ArrayList<E>();
                            }

                        }

                        entityXList = entityXListMap.get(fieldCode);

                        List<E> listAll = (List<E>) collectionMap.get(field.getName());

                        String columnProperty = columnPropertyMap.get(fieldCode);
                        String refColumnProperty = refColumnPropertyMap.get(fieldCode);
                        String inverseColumnProperty = inverseColumnPropertyMap.get(fieldCode);
                        String inverseRefColumnProperty = inverseRefColumnPropertyMap.get(fieldCode);
                        // List<X> entityXList = entityXListMap.get(fieldCode);

                        Collection<E> listForThisEntity = new ArrayList<E>();
                        if (fieldCollectionType == FieldCollectionType.SET) {
                            listForThisEntity = Sets.newHashSet();
                        }

                        for (int k = 0; k < listAll.size(); k++) {
                            E entityE = listAll.get(k);
                            Field entityField, entity2Field, entityXField, entityXField2;
                            try {
                                entityField = entity.getClass().getDeclaredField(columnProperty);
                                entityField.setAccessible(true);
                                Object columnValue = entityField.get(entity);

                                entity2Field = entityE.getClass().getDeclaredField(inverseColumnProperty);
                                entity2Field.setAccessible(true);
                                Object refColumnValue = entity2Field.get(entityE);

                                // table1~table3&&table2~table3
                                for (int x = 0; x < entityXList.size(); x++) {
                                    X entityX = entityXList.get(x);

                                    entityXField = entityX.getClass()
                                            .getDeclaredField(JoinTableUtil.getRefColumnProperty(refColumn));
                                    entityXField.setAccessible(true);
                                    Object columnValueX = entityXField.get(entityX);

                                    entityXField2 = entityX.getClass().getDeclaredField(
                                            JoinTableUtil.getInverseRefColumnProperty(inverseRefColumn));
                                    entityXField2.setAccessible(true);
                                    Object refColumnValueX = entityXField2.get(entityX);

                                    if (columnValueX != null && columnValue != null && refColumnValueX != null
                                            && refColumnValue != null
                                            && columnValueX.toString().equals(columnValue.toString())
                                            && refColumnValueX.toString().equals(refColumnValue.toString())) {
                                        listForThisEntity.add(entityE);
                                    }
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }

                        return (List<E>) listForThisEntity;
                    }

                });

                // 设置代理
                try {
                    field.set(entity, listForThisEntityProxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static <E> List<E> getListResult(Field field) {
        return null;
    }
}
