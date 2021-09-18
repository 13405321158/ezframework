/*
 * @作者: 魏来
 * @日期: 2021/9/13  下午4:15
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.leesky.ezframework.join.interfaces.one2one.One2One;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;

/**
 * <li>描述:
 * <li>作者:魏来
 */
public class QueryMethod extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String ff = tableInfo.getResultMap();
        System.out.println("ff = " + ff);
        String sql = "select " + tableInfo.getKeyColumn() + " from " + tableInfo.getTableName() + " limit 1";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        Field[] fs = tableInfo.getEntityType().getDeclaredFields();
        for (Field f : fs) {
            if (f.isAnnotationPresent(One2One.class)) {
                sql = "select * from " + tableInfo.getTableName()
                        + " where " + tableInfo.getKeyColumn() + "=#{" + tableInfo.getKeyProperty() + "}";

                sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            }
        }

        /* 执行 SQL ，动态 SQL 参考类 SqlMethod */


        return addSelectMappedStatementForTable(mapperClass, "leek", sqlSource, tableInfo);
    }
}
