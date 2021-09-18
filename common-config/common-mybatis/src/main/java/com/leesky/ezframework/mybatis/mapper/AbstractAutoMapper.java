package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
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

    protected Map<String, String[]> entityMap = new HashMap<String, String[]>();

    public <T, E> T oneToMany(T entity) {
        return oneToMany(entity, null, false);
    }

    public <T, E> T oneToMany(T entity, boolean fetchEager) {
        return oneToMany(entity, null, fetchEager);
    }

    public <T, E> T oneToMany(T entity, String... propertyNames) {
        String[] names = propertyNames;
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                oneToMany(entity, names[i], true);
            }
        }

        return entity;
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

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }

                if (field.isAnnotationPresent(OneToMany.class)) {
                    FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getOneToMany().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager == true) {
                        lazy = false;
                    }

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue = null;

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
                        // Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                        Class<?> mapperClass = fc.getMapperClass();
                        // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);

                        List<E> list = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValue));
                        fc.setFieldValueByList(list);
                    } else {
                        boolean needLazyProcessor = false;
                        if (entity.getClass().isAnnotationPresent(AutoLazy.class)
                                && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                            needLazyProcessor = true;
                        }
                        if (!needLazyProcessor) {
                            continue;
                        }

                        final Serializable columnPropertyValueX = columnPropertyValue;
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(ArrayList.class);

                        if (fc.getFieldCollectionType() == FieldCollectionType.SET) {
                            @SuppressWarnings("static-access")
                            Set<E> set = (Set<E>) enhancer.create(Set.class, new LazyLoader() {
                                @Override
                                public Set<E> loadObject() throws Exception {
                                    // Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                                    Class<?> mapperClass = fc.getMapperClass();
                                    // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                    BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                    List<E> list = mapper
                                            .selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));

                                    Set<E> set = null;
                                    if (list != null) {
                                        set = new HashSet<E>();
                                        for (E e : list) {
                                            set.add(e);
                                        }
                                    } else {
                                        set = new HashSet<E>();
                                    }
                                    return set;
                                }

                            });

                            fc.setFieldValueBySet(set);
                        } else {
                            @SuppressWarnings("static-access")
                            List<E> list = (List<E>) enhancer.create(List.class, new LazyLoader() {

                                @Override
                                public List<E> loadObject() throws Exception {
                                    // Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                                    Class<?> mapperClass = fc.getMapperClass();
                                    // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                    BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                    List<E> list = mapper
                                            .selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));

                                    if (list == null || list.size() == 0) {
                                        list = new ArrayList<E>();
                                    }
                                    return list;
                                }

                            });

                            fc.setFieldValueByList(list);
                        }
                    }
                }
            }
        }

        return entity;

    }

    public <T, E> IPage<T> oneToMany(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (entityList == null || entityList.size() == 0) {
            return entityPage;
        }

        entityList = oneToMany(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> List<T> oneToMany(List<T> entityList, String propertyName, boolean fetchEager) {
        if (entityList == null || entityList.size() == 0) {
            return entityList;
        }

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            entityFirst = oneToMany(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.ONETOMANY.name())) {
            return entityList;
        }

        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOMANY.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOMANY.name());
        }

        if (proNames == null || proNames.length == 0) {
            return entityList;
        }

        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName)) {
            return entityList;
        } else if (propertyName != null) {
            proNames = new String[]{propertyName};
        }

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        Map<String, ArrayList<Serializable>> columnPropertyValueListMap = new HashMap<String, ArrayList<Serializable>>();
        Map<String, BaseMapper<E>> mapperMap = new HashMap<String, BaseMapper<E>>();
        Map<String, String> columnMap = new HashMap<String, String>();
        Map<String, String> refColumnMap = new HashMap<String, String>();
        Map<String, String> columnPropertyMap = new HashMap<String, String>();
        Map<String, String> refColumnPropertyMap = new HashMap<String, String>();
        Map<String, FieldCollectionType> fieldCollectionTypeMap = new HashMap<String, FieldCollectionType>();
        Map<String, Boolean> isLazyMap = new HashMap<String, Boolean>();
        for (T entity : entityList) {
            if (entity == null) {
                continue;
            }

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = JoinColumnUtil.getRefColumnProperty(fc);
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue = null;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new OneToManyException("refProperty/refPropertyValue one to many(List) id is not correct!");
                }

                if (!isLazyMap.containsKey(fieldCode)) {
                    isLazyMap.put(fieldCode, lazy);
                }
                if (!columnMap.containsKey(fieldCode)) {
                    columnMap.put(fieldCode, column);
                }
                if (!refColumnMap.containsKey(fieldCode)) {
                    refColumnMap.put(fieldCode, refColumn);
                }
                if (!columnPropertyMap.containsKey(fieldCode)) {
                    columnPropertyMap.put(fieldCode, columnProperty);
                }
                if (!refColumnPropertyMap.containsKey(fieldCode)) {
                    refColumnPropertyMap.put(fieldCode, refColumnProperty);
                }

                if (!columnPropertyValueListMap.containsKey(fieldCode)) {
                    ArrayList<Serializable> arrList = new ArrayList<Serializable>();
                    columnPropertyValueListMap.put(fieldCode, arrList);
                }
                columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!fieldCollectionTypeMap.containsKey(fieldCode)) {
                    fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());
                }
                Class<?> mapperClass = fc.getMapperClass();
                // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                if (!mapperMap.containsKey(fieldCode)) {
                    mapperMap.put(fieldCode, mapper);
                }

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldCode = field.getName();
            boolean lazy = isLazyMap.get(field.getName()).booleanValue();
            String refColumn = refColumnMap.get(field.getName());
            BaseMapper<E> mapper = mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (columnPropertyValueList.size() == 0) {
                continue;
            }

            final OneToManyResult<T, E> oneToManyResult = new OneToManyResult<T, E>(fields);
            oneToManyResult.setList(list);
            oneToManyResult.setLazy(lazy);
            oneToManyResult.setFieldCode(fieldCode);
            oneToManyResult.setRefColumn(refColumn);
            oneToManyResult.setMapperE(mapper);
            oneToManyResult.setMapperMap(mapperMap);
            oneToManyResult.setFieldCollectionType(fieldCollectionType);
            oneToManyResult.setColumnPropertyValueList(columnPropertyValueList);
            oneToManyResult.setColumnPropertyMap(columnPropertyMap);
            oneToManyResult.setRefColumnPropertyMap(refColumnPropertyMap);
            oneToManyResult.setFields(fields);

            if (!lazy) {
                List<E> listAll = mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList));

                if (fieldCollectionType == FieldCollectionType.SET) {
                    Set<E> setAll = null;
                    if (fieldCollectionType == FieldCollectionType.SET) {
                        if (listAll != null) {
                            setAll = Sets.newHashSet(listAll);
                        }
                    }
                    oneToManyResult.setCollectionAll(setAll);
                } else {
                    oneToManyResult.setCollectionAll(listAll);
                }

                oneToManyResult.handle(field);
            } else {// lazy
                boolean needLazyProcessor = false;
                if (entityFirst.getClass().isAnnotationPresent(AutoLazy.class) && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                    needLazyProcessor = true;
                }
                if (!needLazyProcessor) {
                    continue;
                }
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
        String[] names = propertyNames;
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                oneToOne(entity, names[i], true);
            }
        }

        return entity;
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

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(OneToOne.class)) {
                    FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getOneToOne().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager == true) {
                        lazy = false;
                    }

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue = null;

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new OneToOneException("refProperty/refPropertyValue one to one id is not correct!");
                    }

                    if (columnPropertyValue == null) {
                        continue;
                    }

                    if (!lazy) {
                        // Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                        Class<?> mapperClass = fc.getMapperClass();
                        // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                        E e = (E) mapper.selectOne(new QueryWrapper<E>().eq(refColumn, columnPropertyValue));
                        fc.setFieldValueByObject(e);
                    } else {// lazy
                        boolean needLazyProcessor = false;
                        if (entity.getClass().isAnnotationPresent(AutoLazy.class)
                                && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                            needLazyProcessor = true;
                        }
                        if (!needLazyProcessor) {
                            continue;
                        }

                        final Serializable columnPropertyValueX = columnPropertyValue;

                        E e = (E) Enhancer.create(fc.getFieldClass(), new LazyLoader() {

                            @Override
                            public E loadObject() throws Exception {
                                Class<?> mapperClass = fc.getMapperClass();
                                // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                E e = (E) mapper.selectOne(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));

                                if (e == null) {
                                    Class<E> e2Class = (Class<E>) field.getType();
                                    e = e2Class.getDeclaredConstructor().newInstance();
                                }
                                return e;
                            }

                        });

                        fc.setFieldValueByObject(e);
                    }
                }

            }
        }

        return entity;

    }

    public <T, E> IPage<T> oneToOne(IPage<T> entityPage, String propertyName, boolean fetchEager) {
        List<T> entityList = entityPage.getRecords();
        if (entityList == null || entityList.size() == 0) {
            return entityPage;
        }

        entityList = oneToOne(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> List<T> oneToOne(List<T> entityList, String propertyName, boolean fetchEager) {
        if (entityList == null || entityList.size() == 0) {
            return entityList;
        }

        T entityFirst = entityList.get(0);
        if (entityList.size() == 1) {
            oneToOne(entityFirst, propertyName, fetchEager);
            return entityList;
        }

        if (!entityMap.containsKey(entityFirst.getClass().getName() + "." + RelationType.ONETOONE.name())) {
            return entityList;
        }

        Class<?> entityClass = entityFirst.getClass();

        String[] proNames = null;
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.ONETOONE.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.ONETOONE.name());
        }

        if (proNames == null || proNames.length == 0) {
            return entityList;
        }

        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName)) {
            return entityList;
        } else if (propertyName != null) {
            proNames = new String[]{propertyName};
        }

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        Map<String, ArrayList<Serializable>> columnPropertyValueListMap = new HashMap<String, ArrayList<Serializable>>();
        Map<String, BaseMapper<E>> mapperMap = new HashMap<String, BaseMapper<E>>();
        Map<String, String> columnMap = new HashMap<String, String>();
        Map<String, String> refColumnMap = new HashMap<String, String>();
        Map<String, String> columnPropertyMap = new HashMap<String, String>();
        Map<String, String> refColumnPropertyMap = new HashMap<String, String>();
        Map<String, FieldCollectionType> fieldCollectionTypeMap = new HashMap<String, FieldCollectionType>();
        Map<String, Boolean> isLazyMap = new HashMap<String, Boolean>();
        Map<String, Class<?>> fieldClassMap = new HashMap<String, Class<?>>();
        for (T entity : entityList) {
            if (entity == null) {
                continue;
            }

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = fc.getFieldOfRefTableId().getName();
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue = null;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new OneToOneException("refProperty/refPropertyValue one to one(List) id is not correct!");
                }

                if (!fieldClassMap.containsKey(fieldCode)) {
                    fieldClassMap.put(fieldCode, fc.getFieldClass());
                }
                if (!isLazyMap.containsKey(fieldCode)) {
                    isLazyMap.put(fieldCode, lazy);
                }
                if (!columnMap.containsKey(fieldCode)) {
                    columnMap.put(fieldCode, column);
                }
                if (!refColumnMap.containsKey(fieldCode)) {
                    refColumnMap.put(fieldCode, refColumn);
                }
                if (!columnPropertyMap.containsKey(fieldCode)) {
                    columnPropertyMap.put(fieldCode, columnProperty);
                }
                if (!refColumnPropertyMap.containsKey(fieldCode)) {
                    refColumnPropertyMap.put(fieldCode, refColumnProperty);
                }

                if (!columnPropertyValueListMap.containsKey(fieldCode)) {
                    ArrayList<Serializable> arrList = new ArrayList<Serializable>();
                    columnPropertyValueListMap.put(fieldCode, arrList);
                }
                columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!fieldCollectionTypeMap.containsKey(fieldCode)) {
                    fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());
                }
                Class<?> mapperClass = fc.getMapperClass();
                // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                if (!mapperMap.containsKey(fieldCode)) {
                    mapperMap.put(fieldCode, mapper);
                }
            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldCode = field.getName();
            Class<?> fieldClass = fieldClassMap.get(field.getName());
            boolean lazy = isLazyMap.get(field.getName()).booleanValue();
            String refColumn = refColumnMap.get(field.getName());
            BaseMapper<E> mapper = mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (columnPropertyValueList.size() == 0) {
                continue;
            }

            final OneToOneResult<T, E> oneToOneResult = new OneToOneResult<T, E>(fields);
            oneToOneResult.setList(list);
            oneToOneResult.setFieldClass(fieldClass);
            oneToOneResult.setLazy(lazy);
            oneToOneResult.setFieldCode(fieldCode);
            oneToOneResult.setRefColumn(refColumn);
            oneToOneResult.setMapperE(mapper);
            oneToOneResult.setFieldCollectionType(fieldCollectionType);
            oneToOneResult.setColumnPropertyValueList(columnPropertyValueList);
            oneToOneResult.setColumnPropertyMap(columnPropertyMap);
            oneToOneResult.setRefColumnPropertyMap(refColumnPropertyMap);
            oneToOneResult.setFields(fields);

            if (!lazy) {

                List<E> listAll;
                if (columnPropertyValueList.size() == 1) {
                    listAll = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0)));
                } else {
                    listAll = mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList));
                }
                oneToOneResult.setCollectionAll(listAll);

                oneToOneResult.handle(field);
            } else {// lazy

                boolean needLazyProcessor = false;
                if (entityFirst.getClass().isAnnotationPresent(AutoLazy.class) && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true)
                    needLazyProcessor = true;

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
        String[] names = propertyNames;
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                manyToOne(entity, names[i], true);
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

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getManyToOne().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager == true) {
                        lazy = false;
                    }

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue = null;

                    try {
                        Field columnField = entityClass.getDeclaredField(columnProperty);
                        columnField.setAccessible(true);
                        columnPropertyValue = (Serializable) columnField.get(entity);
                    } catch (Exception e) {
                        throw new ManyToOneException("refProperty/refPropertyValue many to one id is not correct!");
                    }

                    if (columnPropertyValue == null) {
                        continue;
                    }

                    if (!lazy) {
                        // Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                        Class<?> mapperClass = fc.getMapperClass();
                        // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                        E e = (E) mapper.selectOne(new QueryWrapper<E>().eq(refColumn, columnPropertyValue));
                        fc.setFieldValueByObject(e);
                    } else {// lazy
                        boolean needLazyProcessor = false;
                        if (entity.getClass().isAnnotationPresent(AutoLazy.class)
                                && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                            needLazyProcessor = true;
                        }
                        if (!needLazyProcessor) {
                            continue;
                        }

                        final Serializable columnPropertyValueX = columnPropertyValue;
                        E e = (E) Enhancer.create(fc.getFieldClass(), new LazyLoader() {
                            @Override
                            public E loadObject() throws Exception {
                                Class<?> mapperClass = fc.getMapperClass();
                                // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                E e = (E) mapper.selectOne(new QueryWrapper<E>().eq(refColumn, columnPropertyValueX));

                                if (e == null) {
                                    Class<E> e2Class = (Class<E>) field.getType();
                                    e = e2Class.getDeclaredConstructor().newInstance();
                                }
                                return e;
                            }

                        });

                        fc.setFieldValueByObject(e);
                    }
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

        entityList = manyToOne(entityList, propertyName, fetchEager);

        return entityPage;
    }

    public <T, E> List<T> manyToOne(List<T> entityList, String propertyName, boolean fetchEager) {
        if (entityList == null || entityList.size() == 0)
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
        if (entityMap.containsKey(entityClass.getName() + "." + RelationType.MANYTOONE.name())) {
            proNames = entityMap.get(entityClass.getName() + "." + RelationType.MANYTOONE.name());
        }

        if (proNames == null || proNames.length == 0) {
            return entityList;
        }

        if (propertyName != null && !Arrays.asList(proNames).contains(propertyName)) {
            return entityList;
        } else if (propertyName != null) {
            proNames = new String[]{propertyName};
        }

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        Map<String, ArrayList<Serializable>> columnPropertyValueListMap = new HashMap<String, ArrayList<Serializable>>();
        Map<String, BaseMapper<E>> mapperMap = new HashMap<String, BaseMapper<E>>();
        Map<String, String> columnMap = new HashMap<String, String>();
        Map<String, String> refColumnMap = new HashMap<String, String>();
        Map<String, String> columnPropertyMap = new HashMap<String, String>();
        Map<String, String> refColumnPropertyMap = new HashMap<String, String>();
        Map<String, FieldCollectionType> fieldCollectionTypeMap = new HashMap<String, FieldCollectionType>();
        Map<String, Boolean> isLazyMap = new HashMap<String, Boolean>();
        Map<String, Class<?>> fieldClassMap = new HashMap<String, Class<?>>();
        for (T entity : entityList) {
            if (entity == null) {
                continue;
            }

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = fc.getFieldOfRefTableId().getName();
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue = null;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new ManyToOneException("refProperty/refPropertyValue many to one(List) id is not correct!");
                }

                if (!fieldClassMap.containsKey(fieldCode)) {
                    fieldClassMap.put(fieldCode, fc.getFieldClass());
                }
                if (!isLazyMap.containsKey(fieldCode)) {
                    isLazyMap.put(fieldCode, lazy);
                }
                if (!columnMap.containsKey(fieldCode)) {
                    columnMap.put(fieldCode, column);
                }
                if (!refColumnMap.containsKey(fieldCode)) {
                    refColumnMap.put(fieldCode, refColumn);
                }
                if (!columnPropertyMap.containsKey(fieldCode)) {
                    columnPropertyMap.put(fieldCode, columnProperty);
                }
                if (!refColumnPropertyMap.containsKey(fieldCode)) {
                    refColumnPropertyMap.put(fieldCode, refColumnProperty);
                }

                if (!columnPropertyValueListMap.containsKey(fieldCode)) {
                    ArrayList<Serializable> arrList = new ArrayList<Serializable>();
                    columnPropertyValueListMap.put(fieldCode, arrList);
                }
                columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!fieldCollectionTypeMap.containsKey(fieldCode)) {
                    fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());
                }
                Class<?> mapperClass = fc.getMapperClass();
                // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                if (!mapperMap.containsKey(fieldCode)) {
                    mapperMap.put(fieldCode, mapper);
                }
            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldCode = field.getName();
            boolean lazy = isLazyMap.get(field.getName()).booleanValue();
            Class<?> fieldClass = fieldClassMap.get(field.getName());
            String refColumn = refColumnMap.get(field.getName());
            BaseMapper<E> mapper = mapperMap.get(field.getName());
            FieldCollectionType fieldCollectionType = fieldCollectionTypeMap.get(field.getName());

            List<Serializable> columnPropertyValueList = columnPropertyValueListMap.get(field.getName());
            List<Serializable> idListDistinct = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (columnPropertyValueList.size() == 0) {
                continue;
            }

            final ManyToOneResult<T, E> manyToOneResult = new ManyToOneResult<T, E>(fields);
            manyToOneResult.setList(list);
            manyToOneResult.setFieldClass(fieldClass);
            manyToOneResult.setLazy(lazy);
            manyToOneResult.setFieldCode(fieldCode);
            manyToOneResult.setRefColumn(refColumn);
            manyToOneResult.setMapperE(mapper);
            manyToOneResult.setFieldCollectionType(fieldCollectionType);
            manyToOneResult.setColumnPropertyValueList(columnPropertyValueList);
            manyToOneResult.setColumnPropertyMap(columnPropertyMap);
            manyToOneResult.setRefColumnPropertyMap(refColumnPropertyMap);
            manyToOneResult.setFields(fields);

            if (!lazy) {

                List<E> listAll;
                if (columnPropertyValueList.size() == 1)
                    listAll = mapper.selectList(new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0)));
                else
                    listAll = mapper.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList));


                manyToOneResult.setCollectionAll(listAll);

                manyToOneResult.handle(field);
            } else {// lazy
                boolean needLazyProcessor = false;
                if (entityFirst.getClass().isAnnotationPresent(AutoLazy.class) && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                    needLazyProcessor = true;
                }
                if (!needLazyProcessor) {
                    continue;
                }

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
        String[] names = propertyNames;
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                manyToMany(entity, names[i], true);
            }
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

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        // Field[] fields = entityClass.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (propertyName != null && !propertyName.equals(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(ManyToMany.class)) {
                    FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                    boolean lazy = false;
                    if (fc.getLazy() == null) {
                        if (fc.getManyToMany().fetch() == FetchType.LAZY) {
                            lazy = true;
                        }
                    } else {
                        lazy = fc.getLazy().value();
                    }

                    if (propertyName != null || fetchEager == true) {
                        lazy = false;
                    }

                    JoinColumn joinColumn = fc.getJoinColumn();
                    String column = JoinColumnUtil.getColumn(fc);
                    String refColumn = JoinColumnUtil.getRefColumn(fc);
                    String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);
                    Serializable columnPropertyValue = null;

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
                        List<Serializable> idList = null;

                        Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
                        Class<?> mapperXClass = fc.getJoinTableMapperClass();

                        // BaseMapper<X> mapperX = (BaseMapper<X>) getMapperBean(mapperXClass);
                        BaseMapper<X> mapperX = (BaseMapper<X>) factory.getObject().getMapper(mapperXClass);
                        List<Object> xIds = mapperX.selectObjs((Wrapper<X>) new QueryWrapper<E>()
                                .select(inverseRefColumn).eq(refColumn, columnPropertyValue));
                        Serializable[] ids = xIds.toArray(new Serializable[]{});
                        idList = Arrays.asList(ids);

                        List<Serializable> idListDistinct = new ArrayList<Serializable>();
                        if (idList.size() > 0) {
                            for (int s = 0; s < idList.size(); s++) {
                                boolean isExists = false;
                                for (int ss = 0; ss < idListDistinct.size(); ss++) {
                                    if (idList.get(s) != null && idListDistinct.get(ss) != null
                                            && idList.get(s).toString().equals(idListDistinct.get(ss).toString())) {
                                        isExists = true;
                                        break;
                                    }
                                }

                                if (!isExists) {
                                    idListDistinct.add(idList.get(s));
                                }
                            }
                        }
                        idList = idListDistinct;

                        if (idList.size() == 0) {
                            continue;
                        }

                        Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                        Class<?> mapperClass = fc.getMapperClass();
                        // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                        BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                        List<E> list = mapper.selectBatchIds(idList);
                        fc.setFieldValueByList(list);
                    } else {// lazy
                        boolean needLazyProcessor = false;
                        if (entity.getClass().isAnnotationPresent(AutoLazy.class)
                                && entity.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true) {
                            needLazyProcessor = true;
                        }
                        if (!needLazyProcessor) {
                            continue;
                        }

                        final Serializable columnPropertyValueX = columnPropertyValue;
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(List.class);

                        // in FieldConditiono.setFieldValueByList() : set to list will call lazy load
                        // fail,so do like this!
                        if (fc.getFieldCollectionType() == FieldCollectionType.SET) {
                            @SuppressWarnings("static-access")
                            Set<E> set = (Set<E>) enhancer.create(Set.class, new LazyLoader() {
                                @Override
                                public Set<E> loadObject() throws Exception {
                                    String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                                    List<Serializable> idList = null;

                                    Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
                                    Class<?> mapperXClass = fc.getJoinTableMapperClass();

                                    // BaseMapper<X> mapperX = (BaseMapper<X>) getMapperBean(mapperXClass);
                                    BaseMapper<X> mapperX = (BaseMapper<X>) factory.getObject().getMapper(mapperXClass);
                                    List<Object> xIds = mapperX.selectObjs((Wrapper<X>) new QueryWrapper<E>()
                                            .select(inverseRefColumn).eq(refColumn, columnPropertyValueX));
                                    Serializable[] ids = xIds.toArray(new Serializable[]{});
                                    idList = Arrays.asList(ids);
                                    List<Serializable> idListDistinct = new ArrayList<Serializable>();
                                    if (idList.size() > 0) {
                                        for (int s = 0; s < idList.size(); s++) {
                                            boolean isExists = false;
                                            for (int ss = 0; ss < idListDistinct.size(); ss++) {
                                                if (idList.get(s) != null && idListDistinct.get(ss) != null && idList
                                                        .get(s).toString().equals(idListDistinct.get(ss).toString())) {
                                                    isExists = true;
                                                    break;
                                                }
                                            }

                                            if (!isExists) {
                                                idListDistinct.add(idList.get(s));
                                            }
                                        }
                                    }
                                    idList = idListDistinct;

                                    if (idList.size() == 0) {
                                        return new HashSet<E>();
                                    }

                                    Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                                    Class<?> mapperClass = fc.getMapperClass();
                                    // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                    BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                    List<E> list = mapper.selectBatchIds(idList);

                                    Set<E> set = null;
                                    if (list != null) {
                                        set = new HashSet<E>();
                                        for (E e : list) {
                                            set.add(e);
                                        }
                                    } else {
                                        set = new HashSet<E>();
                                    }

                                    return set;

                                }

                            });

                            fc.setFieldValueBySet(set);
                        } else {
                            @SuppressWarnings("static-access")
                            List<E> list = (List<E>) enhancer.create(List.class, new LazyLoader() {

                                @Override
                                public List<E> loadObject() throws Exception {
                                    String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                                    List<Serializable> idList = null;

                                    Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
                                    Class<?> mapperXClass = fc.getJoinTableMapperClass();

                                    // BaseMapper<X> mapperX = (BaseMapper<X>) getMapperBean(mapperXClass);
                                    BaseMapper<X> mapperX = (BaseMapper<X>) factory.getObject().getMapper(mapperXClass);
                                    List<Object> xIds = mapperX.selectObjs((Wrapper<X>) new QueryWrapper<E>()
                                            .select(inverseRefColumn).eq(refColumn, columnPropertyValueX));
                                    Serializable[] ids = xIds.toArray(new Serializable[]{});
                                    idList = Arrays.asList(ids);
                                    List<Serializable> idListDistinct = new ArrayList<Serializable>();
                                    if (idList.size() > 0) {
                                        for (int s = 0; s < idList.size(); s++) {
                                            boolean isExists = false;
                                            for (int ss = 0; ss < idListDistinct.size(); ss++) {
                                                if (idList.get(s) != null && idListDistinct.get(ss) != null && idList
                                                        .get(s).toString().equals(idListDistinct.get(ss).toString())) {
                                                    isExists = true;
                                                    break;
                                                }
                                            }

                                            if (!isExists) {
                                                idListDistinct.add(idList.get(s));
                                            }
                                        }
                                    }
                                    idList = idListDistinct;
                                    if (idList.size() == 0) {
                                        return new ArrayList<E>();
                                    }

                                    Class<E> entityClass2 = (Class<E>) fc.getFieldClass();
                                    Class<?> mapperClass = fc.getMapperClass();
                                    // BaseMapper<E> mapper = (BaseMapper<E>) getMapperBean(mapperClass);
                                    BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                                    List<E> proxyList = mapper.selectBatchIds(idList);

                                    if (proxyList == null || proxyList.size() == 0) {
                                        proxyList = new ArrayList<E>();
                                    }

                                    return proxyList;
                                }

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

        entityList = manyToMany(entityList, propertyName, fetchEager);

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

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }


        MapperUtil<T, E, X> maps = new MapperUtil<>().buildMap();
        for (T entity : entityList) {
            if (entity == null) {
                continue;
            }

            for (Field field : fields) {
                String fieldCode = field.getName();

                FieldCondition<T> fc = new FieldCondition<T>(entity, field, fetchEager, factory);
                boolean lazy = fc.getIsLazy();

                JoinColumn joinColumn = fc.getJoinColumn();
                String column = JoinColumnUtil.getColumn(fc);
                String refColumn = JoinColumnUtil.getRefColumn(fc);
                String refColumnProperty = JoinColumnUtil.getRefColumnProperty(fc);
                String inverseRefColumnProperty = JoinColumnUtil.getInverseRefColumnProperty(fc);
                String columnProperty = JoinColumnUtil.getColumnPropertyName(fc);

                Serializable columnPropertyValue = null;
                try {
                    Field columnField = entityClass.getDeclaredField(columnProperty);
                    columnField.setAccessible(true);
                    columnPropertyValue = (Serializable) columnField.get(entity);
                } catch (Exception e) {
                    throw new ManyToManyException("refProperty/refPropertyValue many to many(List) id is not correct!");
                }

                if (!maps.isLazyMap.containsKey(fieldCode)) {
                    maps.isLazyMap.put(fieldCode, lazy);
                }
                if (!maps.columnMap.containsKey(fieldCode)) {
                    maps.columnMap.put(fieldCode, column);
                }
                if (!maps.refColumnMap.containsKey(fieldCode)) {
                    maps.refColumnMap.put(fieldCode, refColumn);
                }
                if (!maps.columnPropertyMap.containsKey(fieldCode)) {
                    maps.columnPropertyMap.put(fieldCode, columnProperty);
                }
                if (!maps.refColumnPropertyMap.containsKey(fieldCode)) {
                    maps.refColumnPropertyMap.put(fieldCode, refColumnProperty);
                }

                if (!maps.columnPropertyValueListMap.containsKey(fieldCode)) {
                    maps.columnPropertyValueListMap.put(fieldCode, Lists.newArrayList());
                }
                maps.columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

                if (!maps.fieldCollectionTypeMap.containsKey(fieldCode)) {
                    maps.fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());
                }

                Class<?> mapperClass = fc.getMapperClass();
                BaseMapper<E> mapper = (BaseMapper<E>) factory.getObject().getMapper(mapperClass);
                if (!maps.mapperMap.containsKey(fieldCode)) {
                    maps.mapperMap.put(fieldCode, mapper);
                }

                Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
                Class<BaseMapper<X>> mapperXClass = (Class<BaseMapper<X>>) fc.getJoinTableMapperClass();
                BaseMapper<X> mapperX = (BaseMapper<X>) factory.getObject().getMapper(mapperXClass);
                if (!maps.entityClassMap.containsKey(fieldCode)) {
                    maps.entityClassMap.put(fieldCode, entityClassX);
                }
                if (!maps.mapperxClassMap.containsKey(fieldCode)) {
                    maps.mapperxClassMap.put(fieldCode, mapperXClass);
                }
                if (!maps.mapperxMap.containsKey(fieldCode)) {
                    maps.mapperxMap.put(fieldCode, mapperX);
                }

                String inverseColumnProperty = InverseJoinColumnUtil.getInverseColumnProperty(fc);
                if (!maps.inverseColumnPropertyMap.containsKey(fieldCode)) {
                    maps.inverseColumnPropertyMap.put(fieldCode, inverseColumnProperty);
                }

                String inverseRefColumn = InverseJoinColumnUtil.getInverseRefColumn(fc);
                if (!maps.inverseRefColumnMap.containsKey(fieldCode)) {
                    maps.inverseRefColumnMap.put(fieldCode, inverseRefColumn);
                }
                if (!maps.inverseRefColumnPropertyMap.containsKey(fieldCode)) {
                    maps.inverseRefColumnPropertyMap.put(fieldCode, inverseRefColumnProperty);
                }

            } // end loop-field

        } // end loop-entity

        List<T> list = Collections.unmodifiableList(entityList);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String fieldCode = field.getName();
            List<Serializable> idListDistinct = Lists.newArrayList();
            String refColumn = maps.refColumnMap.get(field.getName());
            BaseMapper<E> mapper = maps.mapperMap.get(field.getName());
            Boolean lazy = maps.isLazyMap.get(field.getName()).booleanValue();
            FieldCollectionType fieldCollectionType = maps.fieldCollectionTypeMap.get(field.getName());
            List<Serializable> columnPropertyValueList = maps.columnPropertyValueListMap.get(field.getName());

            if (CollectionUtils.isNotEmpty(columnPropertyValueList))
                CommonCode.buildList(idListDistinct, columnPropertyValueList);

            columnPropertyValueList = idListDistinct;

            if (idListDistinct.size() == 0)
                continue;


            BaseMapper<X> mapperX = maps.mapperxMap.get(field.getName());
            String inverseRefColumn = maps.inverseRefColumnMap.get(field.getName());
            String inverseRefColumnProperty = maps.inverseRefColumnPropertyMap.get(field.getName());


            ManyToManyResult<T, E, X> manyToManyResult = new ManyToManyResult<T, E, X>(fields);
            manyToManyResult.build02(maps.columnPropertyMap, maps.refColumnPropertyMap, maps.inverseColumnPropertyMap, maps.inverseRefColumnPropertyMap);
            manyToManyResult.build01(lazy, list, fields, mapper, mapperX, fieldCode, refColumn, inverseRefColumn, fieldCollectionType, columnPropertyValueList);


            Map<String, List<X>> entityXListMap = Maps.newHashMap();

            if (!lazy) {

                List<X> entityXList = mapperX.selectList(new QueryWrapper<X>().in(refColumn, columnPropertyValueList));
                if (!entityXListMap.containsKey(fieldCode)) {
                    entityXListMap.put(fieldCode, entityXList);

                    if (manyToManyResult.getEntityXListMap() == null) {
                        manyToManyResult.setEntityXListMap(entityXListMap);
                    }
                }

                if (entityXList == null || entityXList.size() == 0) {
                    continue;
                }

                columnPropertyValueList = CommonCode.getSerializable(inverseRefColumnProperty, entityXList);

                if (columnPropertyValueList.size() == 0)
                    continue;


                List<E> listAll = mapper.selectList(new QueryWrapper<E>().in(inverseRefColumn, columnPropertyValueList));

                if (fieldCollectionType == FieldCollectionType.SET) {
                    Set<E> setAll = null;
                    if (fieldCollectionType == FieldCollectionType.SET) {
                        if (listAll != null)
                            setAll = Sets.newHashSet(listAll);
                    }
                    manyToManyResult.setCollectionAll(setAll);
                } else {
                    manyToManyResult.setCollectionAll(listAll);
                }

                manyToManyResult.handle(field);
            } else {// lazy
                boolean needLazyProcessor = false;
                if (entityFirst.getClass().isAnnotationPresent(AutoLazy.class) && entityFirst.getClass().getDeclaredAnnotation(AutoLazy.class).value() == true)
                    needLazyProcessor = true;

                if (!needLazyProcessor)
                    continue;

                manyToManyResult.setCollectionAll(null);
                manyToManyResult.handleLazy(field);


            } // end if-lazy
        } // end loop-field

        return list;

    }




}
