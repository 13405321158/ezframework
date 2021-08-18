/**
 * 
 * @author:weilai
 * @Data:2020-8-198:03:35
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>所谓SourceTable表，意思是从这个表中获取数据
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

public @interface SourceTable {
    String tableName();//获取数据的目标表名称
    
    String onColumn();//获取数据的目标表 在中间表对应字段名称

}
