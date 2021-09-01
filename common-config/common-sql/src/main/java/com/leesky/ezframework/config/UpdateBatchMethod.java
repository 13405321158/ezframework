/*
 * @作者: 魏来
 * @日期: 2021年9月1日  下午3:34:46
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.config;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

public class UpdateBatchMethod extends AbstractMethod {

	private static final long serialVersionUID = -642470087674251847L;

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = "<script>\n<foreach collection=\"list\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>";
		String additional = tableInfo.isWithVersion() ? tableInfo.getVersionFieldInfo().getVersionOli("item", "item.") : "" + tableInfo.getLogicDeleteSql(true, true);
		String setSql = sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, "item", "item.");
		String sqlResult = String.format(sql, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(), "item." + tableInfo.getKeyProperty(), additional);
		// log.debug("sqlResult----->{}", sqlResult);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
		// 第三个参数必须和RootMapper的自定义方法名一致
		return this.addUpdateMappedStatement(mapperClass, modelClass, "updateBatch", sqlSource);
	}
}
