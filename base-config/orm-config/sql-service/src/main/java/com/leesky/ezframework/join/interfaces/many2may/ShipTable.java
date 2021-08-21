/*
 * @author:weilai
 * @Data:2020-8-198:04:21
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc:
 *        <li>所谓关系表，意思是 根据此表相关条件 ，关联查询目标数据表
 */

package com.leesky.ezframework.join.interfaces.many2may;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})

public @interface ShipTable {

	
    String tableName();//中间表名称
    
    String onColumn();//中间表参与计算字段名称
    
	String whereColumn();// 当前表参与关联查询字段，即：left join 中的字段
    
}
