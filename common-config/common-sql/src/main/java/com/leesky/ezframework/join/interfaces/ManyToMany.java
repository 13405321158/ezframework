/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */

package com.leesky.ezframework.join.interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ManyToMany {

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午11:33
     * @描述: 另外一个 many方 数据表名称
     **/
    String otherManytableName();//目标表名称

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午11:40
     * @描述: 中间表名称
     **/
    String middleTableName();//中间表名称

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午11:40
     * @描述: 另外一个many方 参与关联数据表字段名称
     **/
    String manyTableColumn();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午11:40
     * @描述: 另外一个many方 在中间表 中的 字段名称
     **/
    String middleTableColumn();


}
