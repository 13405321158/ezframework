/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc:
 */
package com.leesky.ezframework.mybatis.ddl.service;

public interface ImysqlCreateTableService {

    /**
     * @ver: 1.0.0
     * @author: weilai
     * @data:下午4:16:51,2020年2月3日
     * @desc: <li>在 packName 下 查找带有 @TableName注解的 类
     */
    void createMysqlTable(String packName);

    /**
     * 删除表
     *
     * @author: 魏来
     * @date: 2022/2/28 下午1:15
     */
    void delTable(String packName);
}
