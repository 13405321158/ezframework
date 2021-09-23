/*
 * @作者: 魏来
 * @日期: 2021/9/18  上午8:20
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;

/**
 * <li>描述:
 */
@SuppressWarnings("rawtypes")
public class MapperUtil<T, E, X> {
    public Map<String, String> columnMap;
    public Map<String, Boolean> isLazyMap;
    public Map<String, String> refColumnMap;
    public Map<String, JoinTable> joinTableMap;
    public Map<String, Class<X>> entityClassMap;
    public Map<String, Class<?>> fieldClassMap;
    public Map<String, BaseMapper<E>> mapperMap;
    public Map<String, BaseMapper<X>> mapperxMap;
    public Map<String, String> columnPropertyMap;
    public Map<String, String> inverseRefColumnMap;
    public Map<String, String> refColumnPropertyMap;
    public Map<String, String> inverseColumnPropertyMap;
    public Map<String, String> inverseRefColumnPropertyMap;
    public Map<String, Class<BaseMapper<X>>> mapperxClassMap;
    public Map<String, FieldCollectionType> fieldCollectionTypeMap;
    public Map<String, ArrayList<Serializable>> columnPropertyValueListMap;





	public MapperUtil buildMap_o2o() {

        isLazyMap = Maps.newHashMap();
        mapperMap = Maps.newHashMap();
        columnMap = Maps.newHashMap();
        refColumnMap = Maps.newHashMap();
        fieldClassMap = Maps.newHashMap();
        columnPropertyMap = Maps.newHashMap();
        refColumnPropertyMap = Maps.newHashMap();
        fieldCollectionTypeMap = Maps.newHashMap();
        columnPropertyValueListMap = Maps.newHashMap();
        return this;
    }


    public MapperUtil buildMap_m2m() {
        isLazyMap = Maps.newHashMap();
        mapperMap = Maps.newHashMap();
        columnMap = Maps.newHashMap();
        mapperxMap = Maps.newHashMap();
        refColumnMap = Maps.newHashMap();
        joinTableMap = Maps.newHashMap();
        entityClassMap = Maps.newHashMap();
        mapperxClassMap = Maps.newHashMap();
        columnPropertyMap = Maps.newHashMap();
        inverseRefColumnMap = Maps.newHashMap();
        refColumnPropertyMap = Maps.newHashMap();
        fieldCollectionTypeMap = Maps.newHashMap();
        inverseColumnPropertyMap = Maps.newHashMap();
        inverseRefColumnPropertyMap = Maps.newHashMap();
        columnPropertyValueListMap = Maps.newHashMap();
        return this;
    }

    public MapperUtil buildMap_m2o() {

        isLazyMap = Maps.newHashMap();
        mapperMap = Maps.newHashMap();
        columnMap = Maps.newHashMap();
        refColumnMap = Maps.newHashMap();
        fieldClassMap = Maps.newHashMap();
        columnPropertyMap = Maps.newHashMap();
        refColumnPropertyMap = Maps.newHashMap();
        fieldCollectionTypeMap = Maps.newHashMap();
        columnPropertyValueListMap = Maps.newHashMap();

        return this;
    }
    public MapperUtil buildMap_o2m() {

        isLazyMap = Maps.newHashMap();
        mapperMap = Maps.newHashMap();
        columnMap = Maps.newHashMap();
        refColumnMap = Maps.newHashMap();
        columnPropertyMap = Maps.newHashMap();
        refColumnPropertyMap = Maps.newHashMap();
        fieldCollectionTypeMap = Maps.newHashMap();
        columnPropertyValueListMap = Maps.newHashMap();

        return this;
    }
}
