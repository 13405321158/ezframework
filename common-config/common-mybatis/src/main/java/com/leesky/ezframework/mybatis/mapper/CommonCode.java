/*
 * @作者: 魏来
 * @日期: 2021/9/18  下午12:13
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <li>描述: AbstractAutoMappper.java 中通用代码
 */
public class CommonCode {

    public static void buildList(List<Serializable> idListDistinct, List<Serializable> idList) {
        for (Serializable serializable : idList) {
            boolean isExists = false;
            for (Serializable value : idListDistinct) {
                if (serializable != null && value != null && serializable.toString().equals(value.toString())) {
                    isExists = true;
                    break;
                }
            }

            if (serializable != null && !isExists)
                idListDistinct.add(serializable);

        }
    }


    public static <X> List<Serializable> getSerializable(String inverseRefColumnProperty, List<X> entityXList) {
        List<Serializable> columnPropertyValueList;
        List<Serializable> idList = Lists.newArrayList();

        extracted(inverseRefColumnProperty, entityXList, idList);
        List<Serializable> idListDistinct = Lists.newArrayList();
        if (idList.size() > 0)
            CommonCode.buildList(idListDistinct, idList);

        columnPropertyValueList = idListDistinct;
        return columnPropertyValueList;
    }

    public static <X> void extracted(String inverseRefColumnProperty, List<X> entityXList, List<Serializable> idList) {
        for (X x : entityXList) {
            try {
                Field fieldX = x.getClass().getDeclaredField(inverseRefColumnProperty);
                fieldX.setAccessible(true);
                Serializable id = (Serializable) fieldX.get(x);
                if (id != null && !idList.contains(id))
                    idList.add(id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Field[] buildField(String[] proNames, Class<?> entityClass) {

        Field[] fields = new Field[proNames.length];
        for (int i = 0; i < proNames.length; i++) {
            try {
                fields[i] = entityClass.getDeclaredField(proNames[i]);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        return fields;
    }

    public static <T, E> void extracted(MapperUtil<T, E, ?> maps, String fieldCode, FieldCondition<T> fc, boolean lazy,
                                        String column, String refColumn, String refColumnProperty, String columnProperty,
                                        Serializable columnPropertyValue, ObjectFactory<SqlSession> factory) {
        if (!maps.isLazyMap.containsKey(fieldCode))
            maps.isLazyMap.put(fieldCode, lazy);

        if (!maps.columnMap.containsKey(fieldCode))
            maps.columnMap.put(fieldCode, column);

        if (!maps.refColumnMap.containsKey(fieldCode))
            maps.refColumnMap.put(fieldCode, refColumn);

        if (!maps.columnPropertyMap.containsKey(fieldCode))
            maps.columnPropertyMap.put(fieldCode, columnProperty);

        if (!maps.refColumnPropertyMap.containsKey(fieldCode))
            maps.refColumnPropertyMap.put(fieldCode, refColumnProperty);


        if (!maps.columnPropertyValueListMap.containsKey(fieldCode))
            maps.columnPropertyValueListMap.put(fieldCode, Lists.newArrayList());

        maps.columnPropertyValueListMap.get(fieldCode).add(columnPropertyValue);

        if (!maps.fieldCollectionTypeMap.containsKey(fieldCode))
            maps.fieldCollectionTypeMap.put(fieldCode, fc.getFieldCollectionType());

        if (!maps.mapperMap.containsKey(fieldCode))
            maps.mapperMap.put(fieldCode, (BaseMapper<E>) factory.getObject().getMapper(fc.getMapperClass()));
    }

    public static <T, E, X> Serializable[] getSerializable(FieldCondition<T> fc, String refColumn, Serializable columnPropertyValue,
                                                           String inverseRefColumn, ObjectFactory<SqlSession> factory) {
        Class<X> entityClassX = (Class<X>) fc.getJoinTable().entityClass();
        Class<?> mapperXClass = fc.getJoinTableMapperClass();

        BaseMapper<X> mapperX = (BaseMapper<X>) factory.getObject().getMapper(mapperXClass);
        List<Object> xIds = mapperX.selectObjs((Wrapper<X>) new QueryWrapper<E>()
                .select(inverseRefColumn).eq(refColumn, columnPropertyValue));
        return xIds.toArray(new Serializable[]{});
    }


}
