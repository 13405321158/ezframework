package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.enums.FetchType;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import com.leesky.ezframework.mybatis.enums.RelationType;
import com.leesky.ezframework.mybatis.exception.ManyToManyException;
import com.leesky.ezframework.mybatis.exception.ManyToOneException;
import com.leesky.ezframework.mybatis.exception.OneToManyException;
import com.leesky.ezframework.mybatis.exception.OneToOneException;
import com.leesky.ezframework.mybatis.result.ManyToManyResult;
import com.leesky.ezframework.mybatis.result.ManyToOneResult;
import com.leesky.ezframework.mybatis.result.OneToManyResult;
import com.leesky.ezframework.mybatis.result.OneToOneResult;
import com.leesky.ezframework.mybatis.utils.InverseJoinColumnUtil;
import com.leesky.ezframework.mybatis.utils.JoinColumnUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings({"unused", "unchecked"})
public abstract class AbstractAutoMapper {
    @Autowired
    ObjectFactory<SqlSession> factory;

    protected Map<String, String[]> entityMap = Maps.newHashMap();

    public <T, E> T oneToMany(T entity) {
        return oneToMany(entity, null, false);
    }

    public <T, E> T oneToMany(T entity, boolean fetchEager) {
        return oneToMany(entity, null, fetchEager);
    }

    public <T, E> T oneToMany(T entity, String... propertyNames) {
        if (ArrayUtils.isNotEmpty(propertyNames)) {
            for (String str : propertyNames)
                oneToMany(entity, str, true);

        }

        return entity;
    }

    public <T, E> IPage<T> oneToMany(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList))
            return entityPage;

        oneToMany(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> T oneToMany(T entity, String propertyName, boolean fetchEager) {
        if (!entityMap.containsKey(entity.getClass().getName() + "." + RelationType.ONETOMANY.name())) {
            return entity;
        }

        Class<?> entityClass = entity.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOMANY.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOMANY.name());
        }

        assert proNames != null;
        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }

                if (field.isAnnotationPresent(OneToMany.class)) {
                    FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getOneToMany().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager) {
                        lazy = false;
                    }
                    Serializable columnPropertyValue;
                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new OneToManyException("refProperty/refPropertyValue one to many id is not correct!");
                    }

                    if (columnPropertyValue == null) {
                        continue;
                    }

                    if (!lazy) {
                        Class<?> mapperClass = fc.getMapperClass();

                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);

                        List<E> list = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValue));
                        fc.setFieldValueByList(list);
                    } else {
                        boolean needLazyProcessor = entity.getClass().isAnnotationPresent(AutoLazy.class) && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value();
                        if (!needLazyProcessor)
                            continue;

                        final Serializable columnPropertyValueX = columnPropertyValue;
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(ArrayList.class);

                        if (fc.getFieldCollectionType() == FieldCollectionType.SET) {
                            @SuppressWarnings("static-access")
                            Set<E> set = (Set<E>) enhancer.create(Set.class, (LazyLoader) () -> {
                                Class<?> mapperClass = fc.getMapperClass();
                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                List<E> list = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));
                                return CollectionUtils.isEmpty(list) ? Sets.newHashSet() : Sets.newHashSet(list);
                            });

                            fc.setFieldValueBySet(set);
                        } else {
                            @SuppressWarnings("static-access")
                            List<E> list = (List<E>) enhancer.create(List.class, (LazyLoader) () -> {
                                Class<?> mapperClass = fc.getMapperClass();
                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                List<E> ret = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));
                                if (CollectionUtils.isEmpty(ret))
                                    ret = Lists.newArrayList();
                                return ret;
                            });

                            fc.setFieldValueByList(list);
                        }
                    }
                }
            }
        }

        return entity;

    }

    public <T, E> List<T> oneToMany(List<T> entityList, String propertyName, boolean fetchEager) {
        if (CollectionUtils.isEmpty(entityList))
            return entityList;

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            oneToMany(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.ONETOMANY.name()))
            return entityList;


        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOMANY.name()))
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOMANY.name());


        if (ArrayUtils.isEmpty(proNames))
            return entityList;

        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName))
            return entityList;
        else if (propertyName != null)
            proNames = new String[]{propertyName};


        Field[] fields = CommonCode.buildField(proNames, entityClass);

        MapperUtil<T, E, ?> o2mMaps = new MapperUtil<>().buildMap_o2m();
        for (T entity : entityList) {
            if (entity == null)
                continue;

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = JoinColumnUtil.getRefColumnProperty(fc);
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new OneToManyException("refProperty/refPropertyValue one to many(List) id is not correct!");
                }

                CommonCode.extracted(o2mMaps, fieldCode, fc, lazy, column, refColumn, refColumnProperty, columnProperty, columnPropertyValue, factory);

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldCode = field.getName();
            boolean lazy = o2mMaps.isLazyMap.get(field.getName());
            String refColumn = o2mMaps.refColumnMap.get(field.getName());
            BaseMapper<E> mapper = o2mMaps.mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = o2mMaps.fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = o2mMaps.columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            if (idListDistinct.size() == 0)
                continue;

            OneToManyResult<T, E> oneToManyResult = new OneToManyResult<>(fields);
            oneToManyResult.setList(list);
            oneToManyResult.setFieldCode(fieldCode);
            oneToManyResult.setRefColumn(refColumn);
            oneToManyResult.setMapperE(mapper);

            oneToManyResult.setFieldCollectionType(fieldCollectionType);
            oneToManyResult.setColumnPropertyValueList(idListDistinct);
            oneToManyResult.setColumnPropertyMap(o2mMaps.columnPropertyMap);
            oneToManyResult.setRefColumnPropertyMap(o2mMaps.refColumnPropertyMap);


            if (!lazy) {
                QueryWrapper<E> filter = new QueryWrapper<E>().in(refColumn, idListDistinct);
                List<E> listAll = mapper.selectList(filter);

                if (fieldCollectionType == FieldCollectionType.SET) {
                    Set<E> setAll = listAll != null ? Sets.newHashSet(listAll) : Sets.newHashSet();
                    oneToManyResult.setCollectionAll(setAll);
                } else {
                    listAll = listAll != null ? listAll : Lists.newArrayList();
                    oneToManyResult.setCollectionAll(listAll);
                }
                oneToManyResult.handle(field);
            } else {// lazy
                boolean needLazyProcessor = entityFirst.getClass().isAnnotationPresent(AutoLazy.class) && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value();
                if (!needLazyProcessor)
                    continue;

                oneToManyResult.setCollectionAll(null);
                oneToManyResult.handleLazy(field);
            } // end if-lazy
        } // end loop-field

        return list;

    }

    public <T, E> T oneToOne(T entity) {
        return oneToOne(entity, null, false);
    }

    public <T, E> T oneToOne(T entity, boolean fetchEager) {
        return oneToOne(entity, null, fetchEager);
    }

    public <T, E> T oneToOne(T entity, String propertyName) {
        return oneToOne(entity, propertyName, true);
    }

    public <T, E> T oneToOne(T entity, String... propertyNames) {
        if (ArrayUtils.isNotEmpty(propertyNames)) {
            for (String propertyName : propertyNames)
                oneToOne(entity, propertyName, true);

        }

        return entity;
    }

    public <T, E> IPage<T> oneToOne(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (entityList == null || entityList.size() == 0) {
            return entityPage;
        }

        oneToOne(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> T oneToOne(T entity, String propertyName, boolean fetchEager) {
        if (!entityMap.containsKey(entity.getClass().getName() + "." + RelationType.ONETOONE.name())) {
            return entity;
        }

        Class<?> entityClass = entity.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOONE.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOONE.name());
        }

        assert proNames != null;
        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(OneToOne.class)) {
                    FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getOneToOne().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager) {
                        lazy = false;
                    }

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue;

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new OneToOneException("refProperty/refPropertyValue one to one id is not correct!");
                    }

                    CommonCode.extracted(entity, field, fc, lazy, refColumn, columnPropertyValue, factory);
                }

            }
        }

        return entity;

    }

    public <T, E> List<T> oneToOne(List<T> entityList, String propertyName, boolean fetchEager) {
        if (CollectionUtils.isEmpty(entityList))
            return entityList;

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            oneToOne(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.ONETOONE.name()))
            return entityList;

        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOONE.name()))
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOONE.name());

        if (ArrayUtils.isEmpty(proNames))
            return entityList;


        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName))
            return entityList;
        else if (propertyName != null)
            proNames = new String[]{propertyName};


        Field[] fields = CommonCode.buildField(proNames, entityClass);

        MapperUtil<T, E, ?> o2oMaps = new MapperUtil<>().buildMap_o2o();
        for (T entity : entityList) {
            if (entity == null)
                continue;

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = fc.getFieldOfRefTableId().getName();
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new OneToOneException("refProperty/refPropertyValue one to one(List) id is not correct!");
                }

                if (!o2oMaps.fieldClassMap.containsKey(fieldCode))
                    o2oMaps.fieldClassMap.put(fieldCode, fc.getFieldClass());

                if (!o2oMaps.isLazyMap.containsKey(fieldCode))
                    o2oMaps.isLazyMap.put(fieldCode, lazy);

                if (!o2oMaps.columnMap.containsKey(fieldCode))
                    o2oMaps.columnMap.put(fieldCode, column);

                if (!o2oMaps.refColumnMap.containsKey(fieldCode))
                    o2oMaps.refColumnMap.put(fieldCode, refColumn);

                if (!o2oMaps.columnPropertyMap.containsKey(fieldCode))
                    o2oMaps.columnPropertyMap.put(fieldCode, columnProperty);

                if (!o2oMaps.refColumnPropertyMap.containsKey(fieldCode))
                    o2oMaps.refColumnPropertyMap.put(fieldCode, refColumnProperty);


                if (!o2oMaps.columnPropertyValueListMap.containsKey(fieldCode))
                    o2oMaps.columnPropertyValueListMap.put(fieldCode, Lists.newArrayList());

                o2oMaps.columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!o2oMaps.fieldCollectionTypeMap.containsKey(fieldCode))
                    o2oMaps.fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());


                if (!o2oMaps.mapperMap.containsKey(fieldCode))
                    o2oMaps.mapperMap.put(fieldCode, (BaseMapper<E>) factory.getObject().getMapper(fc.getMapperClass()));

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldCode = field.getName();
            Class<?> fieldClass = o2oMaps.fieldClassMap.get(field.getName());
            boolean lazy = o2oMaps.isLazyMap.get(field.getName());
            String refColumn = o2oMaps.refColumnMap.get(field.getName());
            BaseMapper<E> mapper = o2oMaps.mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = o2oMaps.fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = o2oMaps.columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (columnPropertyValueList.size() == 0)
                continue;

            OneToOneResult<T, E> oneToOneResult = new OneToOneResult<>(fields);
            oneToOneResult.build01(list, fieldCode, refColumn, fieldClass);
            oneToOneResult.build02(mapper, fieldCollectionType, columnPropertyValueList, o2oMaps);

            if (!lazy) {

                List<E> listAll;
                if (columnPropertyValueList.size() == 1)
                    listAll = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0)));
                else
                    listAll = mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList));

                oneToOneResult.setCollectionAll(listAll);

                oneToOneResult.handle(field);
            } else {// lazy

                boolean needLazyProcessor = entityFirst.getClass().isAnnotationPresent(AutoLazy.class)
                        && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value();

                if (!needLazyProcessor)
                    continue;

                oneToOneResult.setCollectionAll(null);
                oneToOneResult.handleLazy(field);

            } // end if-lazy
        } // end loop-field

        return list;

    }

    public <T, E> T manyToOne(T entity) {
        return manyToOne(entity, null, false);
    }

    public <T, E> T manyToOne(T entity, boolean fetchEager) {
        return manyToOne(entity, null, fetchEager);
    }

    public <T, E> T manyToOne(T entity, String propertyName) {
        return manyToOne(entity, propertyName, true);
    }

    public <T, E> T manyToOne(T entity, String... propertyNames) {
        if (ArrayUtils.isNotEmpty(propertyNames)) {
            for (String propertyName : propertyNames) {
                manyToOne(entity, propertyName, true);
            }
        }

        return entity;
    }

    public <T, E> T manyToOne(T entity, String propertyName, boolean fetchEager) {
        if (!entityMap.containsKey(entity.getClass().getName() + "." + RelationType.MANYTOONE.name())) {
            return entity;
        }

        Class<?> entityClass = entity.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.MANYTOONE.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.MANYTOONE.name());
        }

        assert proNames != null;
        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null)
                        if (fc.getManyToOne().fetch() == FetchType.LAZY)
                            lazy = true;

                        else
                            lazy = fc.getLazy().value();

                    if (propertyName != null || fetchEager)
                        lazy = false;

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue;

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new ManyToOneException("refProperty/refPropertyValue many to one id is not correct!");
                    }
                    CommonCode.extracted(entity, field, fc, lazy, refColumn, columnPropertyValue, factory);

                }

            }
        }

        return entity;
    }

    public <T, E> IPage<T> manyToOne(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (entityList == null || entityList.size() == 0) {
            return entityPage;
        }

        manyToOne(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> List<T> manyToOne(List<T> entityList, String propertyName, boolean fetchEager) {
        if (CollectionUtils.isEmpty(entityList))
            return entityList;

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            manyToOne(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.MANYTOONE.name())) {
            return entityList;
        }

        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.MANYTOONE.name()))
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.MANYTOONE.name());


        if (ArrayUtils.isEmpty(proNames))
            return entityList;


        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName))
            return entityList;
        else if (propertyName != null)
            proNames = new String[]{propertyName};

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        MapperUtil<T, E, ?> m2oMaps = new MapperUtil<>().buildMap_m2o();
        for (T entity : entityList) {
            if (entity == null)
                continue;

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = fc.getFieldOfRefTableId().getName();
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new ManyToOneException("refProperty/refPropertyValue many to one(List) id is not correct!");
                }

                if (!m2oMaps.fieldClassMap.containsKey(fieldCode))
                    m2oMaps.fieldClassMap.put(fieldCode, fc.getFieldClass());

                if (!m2oMaps.isLazyMap.containsKey(fieldCode))
                    m2oMaps.isLazyMap.put(fieldCode, lazy);

                if (!m2oMaps.columnMap.containsKey(fieldCode))
                    m2oMaps.columnMap.put(fieldCode, column);

                if (!m2oMaps.refColumnMap.containsKey(fieldCode))
                    m2oMaps.refColumnMap.put(fieldCode, refColumn);

                if (!m2oMaps.columnPropertyMap.containsKey(fieldCode))
                    m2oMaps.columnPropertyMap.put(fieldCode, columnProperty);

                if (!m2oMaps.refColumnPropertyMap.containsKey(fieldCode))
                    m2oMaps.refColumnPropertyMap.put(fieldCode, refColumnProperty);


                if (!m2oMaps.columnPropertyValueListMap.containsKey(fieldCode))
                    m2oMaps.columnPropertyValueListMap.put(fieldCode, Lists.newArrayList());

                m2oMaps.columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!m2oMaps.fieldCollectionTypeMap.containsKey(fieldCode))
                    m2oMaps.fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());

                if (!m2oMaps.mapperMap.containsKey(fieldCode))
                    m2oMaps.mapperMap.put(fieldCode, (BaseMapper<E>) factory.getObject().getMapper(fc.getMapperClass()));

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldCode = field.getName();
            boolean lazy = m2oMaps.isLazyMap.get(field.getName());
            Class<?> fieldClass = m2oMaps.fieldClassMap.get(field.getName());
            String refColumn = m2oMaps.refColumnMap.get(field.getName());
            BaseMapper<E> mapper = m2oMaps.mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = m2oMaps.fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = m2oMaps.columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);


            if (idListDistinct.size() == 0)
                continue;

            ManyToOneResult<T, E> manyToOneResult = new ManyToOneResult<>(fields);
            manyToOneResult.setList(list);
            manyToOneResult.setMapperE(mapper);
            manyToOneResult.setFieldCode(fieldCode);
            manyToOneResult.setRefColumn(refColumn);
            manyToOneResult.setFieldClass(fieldClass);

            manyToOneResult.setColumnPropertyValueList(idListDistinct);
            manyToOneResult.setColumnPropertyMap(m2oMaps.columnPropertyMap);
            manyToOneResult.setRefColumnPropertyMap(m2oMaps.refColumnPropertyMap);


            if (!lazy) {

                List<E> listAll;
                if (columnPropertyValueList.size() == 1)
                    listAll = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0)));
                else
                    listAll = mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList));

                manyToOneResult.setCollectionAll(listAll);

                manyToOneResult.handle(field);
            } else {// lazy
                boolean needLazyProcessor = entityFirst.getClass().isAnnotationPresent(AutoLazy.class)
                        && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value();
                if (!needLazyProcessor)
                    continue;

                manyToOneResult.setCollectionAll(null);
                manyToOneResult.handleLazy(field);

            } // end if-lazy
        } // end loop-field

        return list;

    }

    public <T, E, X> T manyToMany(T entity) {
        return manyToMany(entity, null, false);
    }

    public <T, E, X> T manyToMany(T entity, boolean fetchEager) {
        return manyToMany(entity, null, fetchEager);
    }

    public <T, E, X> T manyToMany(T entity, String propertyName) {
        return manyToMany(entity, propertyName, true);
    }

    public <T, E, X> T manyToMany(T entity, String... propertyNames) {
        if (ArrayUtils.isNotEmpty(propertyNames)) {
            for (String name : propertyNames)
                manyToMany(entity, name, true);

        }

        return entity;
    }

    public <T, E, X> T manyToMany(T entity, String propertyName, boolean fetchEager) {
        if (!entityMap.containsKey(entity.getClass().getName() + "." + RelationType.MANYTOMANY.name())) {
            return entity;
        }

        Class<?> entityClass = entity.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.MANYTOMANY.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.MANYTOMANY.name());
        }

        assert proNames != null;
        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(ManyToMany.class)) {
                    FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getManyToMany().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager) {
                        lazy = false;
                    }
                    Serializable columnPropertyValue;
                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new ManyToManyException("refProperty/refPropertyValue many to many id is not correct!");
                    }

                    if (columnPropertyValue == null) {
                        continue;
                    }

                    if (!lazy) {
                        String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                        List<Serializable> idList;

                        Serializable[] ids = CommonCode.getSerializable(fc, refColumn, columnPropertyValue, inverseRefColumn, factory);
                        idList = Arrays.asList(ids);

                        List<Serializable> idListDistinct = Lists.newArrayList();
                        if (idList.size() > 0)
                            CommonCode.buildList(idListDistinct, idList);

                        idList = idListDistinct;

                        if (idList.size() == 0)
                            continue;

                        Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                        Class<?> mapperClass = fc.getMapperClass();
                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                        List<E> list = mapper.selectBatchIds(idList);
                        fc.setFieldValueByList(list);
                    } else {// lazy
                        boolean needLazyProcessor = entity.getClass().isAnnotationPresent(AutoLazy.class) && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value();
                        if (!needLazyProcessor) {
                            continue;
                        }

                        final Serializable columnPropertyValueX = columnPropertyValue;
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(List.class);

                        if (fc.getFieldCollectionType() == FieldCollectionType.SET) {
                            @SuppressWarnings("static-access")
                            Set<E> set = (Set<E>) enhancer.create(Set.class, (LazyLoader) () -> {
                                String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                                Serializable[] ids = CommonCode.getSerializable(fc, refColumn, columnPropertyValueX, inverseRefColumn, factory);
                                List<Serializable> idList = Arrays.asList(ids);
                                List<Serializable> idListDistinct = Lists.newArrayList();
                                if (CollectionUtils.isNotEmpty(idList))
                                    CommonCode.buildList(idListDistinct, idList);

                                idList = idListDistinct;

                                if (CollectionUtils.isEmpty(idList))
                                    return Sets.newHashSet();

                                Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                                Class<?> mapperClass = fc.getMapperClass();
                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                List<E> list = mapper.selectBatchIds(idList);
                                return list != null ? Sets.newHashSet(list) : Sets.newHashSet();
                            });

                            fc.setFieldValueBySet(set);
                        } else {
                            @SuppressWarnings("static-access")
                            List<E> list = (List<E>) enhancer.create(List.class, (LazyLoader) () -> {
                                String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                                Serializable[] ids = CommonCode.getSerializable(fc, refColumn, columnPropertyValueX, inverseRefColumn, factory);
                                List<Serializable> idList = Arrays.asList(ids);
                                List<Serializable> idListDistinct = Lists.newArrayList();
                                if (CollectionUtils.isNotEmpty(idList))
                                    CommonCode.buildList(idListDistinct, idList);

                                idList = idListDistinct;
                                if (idList.size() == 0)
                                    return Lists.newArrayList();

                                Class<?> mapperClass = fc.getMapperClass();
                                Class<E> entityClass2 = (Class<E>) fc.getFieldClass();

                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                List<E> proxyList = mapper.selectBatchIds(idList);

                                return CollectionUtils.isEmpty(proxyList) ? Lists.newArrayList() : proxyList;

                            });

                            fc.setFieldValueByList(list);
                        }
                    }

                }

            }
        }

        return entity;
    }

    public <T, E, X> IPage<T> manyToMany(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (entityList == null || entityList.size() == 0) {
            return entityPage;
        }

        manyToMany(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E, X> List<T> manyToMany(List<T> entityList, String propertyName, boolean fetchEager) {
        if (CollectionUtils.isEmpty(entityList))
            return entityList;

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            manyToMany(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.MANYTOMANY.name()))
            return entityList;

        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.MANYTOMANY.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.MANYTOMANY.name());
        }

        if (proNames == null || proNames.length == 0) {
            return entityList;
        }

        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName)) {
            return entityList;
        } else if (propertyName != null) {
            proNames = new String[]{propertyName};
        }

        Field[] fields = CommonCode.buildField(proNames, entityClass);

        MapperUtil<T, E, X> m2mMaps = new MapperUtil<>().buildMap_m2m();
        for (T entity : entityList) {
            if (entity == null) {
                continue;
            }

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();
                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = JoinColumnUtil.getRefColumnProperty(fc);
                String inverseRefColumnProperty = JoinColumnUtil.getInverseRefColumnProperty(fc);
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new ManyToManyException("refProperty/refPropertyValue many to many(List) id is not correct!");
                }

                CommonCode.extracted(m2mMaps, fieldCode, fc, lazy, column, refColumn, refColumnProperty, columnProperty, columnPropertyValue, factory);

                Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
                Class<BaseMapper<X>> mapperXClass = (Class<BaseMapper<X>>) fc.getJoinTableMapperClass();
                BaseMapper<X> mapperX = factory.getObject().getMapper(mapperXClass);
                if (!m2mMaps.entityClassMap.containsKey(fieldCode)) {
                    m2mMaps.entityClassMap.put(fieldCode, entityClassX);
                }
                if (!m2mMaps.mapperxClassMap.containsKey(fieldCode)) {
                    m2mMaps.mapperxClassMap.put(fieldCode, mapperXClass);
                }
                if (!m2mMaps.mapperxMap.containsKey(fieldCode)) {
                    m2mMaps.mapperxMap.put(fieldCode, mapperX);
                }

                String inverseColumnProperty = InverseJoinColumnUtil.getInverseColumnProperty(fc);
                if (!m2mMaps.inverseColumnPropertyMap.containsKey(fieldCode)) {
                    m2mMaps.inverseColumnPropertyMap.put(fieldCode, inverseColumnProperty);
                }

                String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                if (!m2mMaps.inverseRefColumnMap.containsKey(fieldCode)) {
                    m2mMaps.inverseRefColumnMap.put(fieldCode, inverseRefColumn);
                }
                if (!m2mMaps.inverseRefColumnPropertyMap.containsKey(fieldCode)) {
                    m2mMaps.inverseRefColumnPropertyMap.put(fieldCode, inverseRefColumnProperty);
                }

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldCode = field.getName();
            List<Serializable> idListDistinct = Lists.newArrayList();
            String refColumn = m2mMaps.refColumnMap.get(field.getName());
            BaseMapper<E> mapper = m2mMaps.mapperMap.get(field.getName());
            boolean lazy = m2mMaps.isLazyMap.get(field.getName());

            FieldCollectionType fieldCollectionType = m2mMaps.fieldCollectionTypeMap.get(field.getName());
            List<Serializable> columnPropertyValueList = m2mMaps.columnPropertyValueListMap.get(field.getName());

            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (idListDistinct.size() == 0)
                continue;

            BaseMapper<X> mapperX = m2mMaps.mapperxMap.get(field.getName());
            String inverseRefColumn = m2mMaps.inverseRefColumnMap.get(field.getName());
            String inverseRefColumnProperty = m2mMaps.inverseRefColumnPropertyMap.get(field.getName());

            ManyToManyResult<T, E, X> manyToManyResult = new ManyToManyResult<>(fields);
            manyToManyResult.build02(m2mMaps.columnPropertyMap, m2mMaps.refColumnPropertyMap, m2mMaps.inverseColumnPropertyMap, m2mMaps.inverseRefColumnPropertyMap);
            manyToManyResult.build01(lazy, list, fields, mapper, mapperX, fieldCode, refColumn, inverseRefColumn, fieldCollectionType, columnPropertyValueList);

            Map<String, List<X>> entityXListMap = Maps.newHashMap();

            if (!lazy) {

                List<X> entityXList = mapperX.selectList(new QueryWrapper<X>().in(refColumn, columnPropertyValueList));
                entityXListMap.put(fieldCode, entityXList);

                if (manyToManyResult.getEntityXListMap() == null)
                    manyToManyResult.setEntityXListMap(entityXListMap);


                if (CollectionUtils.isEmpty(entityXList)) {
                    continue;
                }

                columnPropertyValueList = CommonCode.getSerializable(inverseRefColumnProperty, entityXList);

                if (columnPropertyValueList.size() == 0)
                    continue;

                List<E> listAll = mapper.selectList(new QueryWrapper<E>().in(inverseRefColumn, columnPropertyValueList));

                if (fieldCollectionType == FieldCollectionType.SET) {
                    Set<E> setAll = listAll != null ? Sets.newHashSet(listAll) : null;
                    manyToManyResult.setCollectionAll(setAll);
                } else
                    manyToManyResult.setCollectionAll(listAll);

                manyToManyResult.handle(field);
            } else {// lazy
                Class<?> c = entityFirst.getClass();
                boolean needLazyProcessor = c.isAnnotationPresent(AutoLazy.class) && c.getDeclaredAnnotation(AutoLazy.class).value();

                if (!needLazyProcessor)
                    continue;

                manyToManyResult.setCollectionAll(null);
                try {
                    manyToManyResult.handleLazy(field);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } // end if-lazy
        } // end loop-field

        return list;

    }

}
