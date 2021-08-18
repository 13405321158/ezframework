package com.leesky.ezframework.ddl.service;

/**
 * 
 * @author: weilai
 * @Data:上午9:58:47,2020年2月2日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */
public interface ImysqlCreateTableService {

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:下午4:16:51,2020年2月3日
	 * @desc:
	 *        <li>在 packName 下 查找带有 @TableName注解的 类
	 *
	 */
	public void createMysqlTable(String packName);

}
