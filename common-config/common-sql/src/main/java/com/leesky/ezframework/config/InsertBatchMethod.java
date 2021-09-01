/*
 * @作者: 魏来
 * @日期: 2021年9月1日  下午2:56:58
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.config;

import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

public class InsertBatchMethod extends AbstractMethod {

	private static final long serialVersionUID = -3407433370096327456L;

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = "<script>insert into %s %s values %s</script>";
		String fieldSql = prepareFieldSql(tableInfo);
		String valueSql = prepareValuesSql(tableInfo);
		String sqlResult = String.format(sql, tableInfo.getTableName(), fieldSql, valueSql);

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, "insertBatch", sqlSource, new NoKeyGenerator(), null, null);
	}

	private String prepareFieldSql(TableInfo tableInfo) {
		StringBuilder fieldSql = new StringBuilder();
		fieldSql.append(tableInfo.getKeyColumn()).append(",");
		tableInfo.getFieldList().forEach(x -> fieldSql.append(x.getColumn()).append(","));
		fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
		fieldSql.insert(0, "(");
		fieldSql.append(")");
		return fieldSql.toString();
	}

	private String prepareValuesSql(TableInfo tableInfo) {
		final StringBuilder valueSql = new StringBuilder();
		valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
		valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
		tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
		valueSql.delete(valueSql.length() - 1, valueSql.length());
		valueSql.append("</foreach>");
		return valueSql.toString();
	}

}
