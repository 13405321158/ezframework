/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */

package com.leesky.ezframework.join.interfaces.many2many;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Many2Many {

    /**
     * <li>中间表.表名称
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午11:40
     **/
    String middleTableName();// 中间表名称

    /**
     * <li>当前Bean在中间表中的字段名称
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午11:40
     **/
    String middleTableColumn();

    /**
     * <li>另外一个many方在中间表中的字段名称
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午11:40
     **/
    String otherMiddleTableColumn();

    /**
     * <li>:另外一个many方的 表名称,查询专用
     *
     * @作者: 魏来
     * @日期: 2021/9/10  下午3:58
     **/
    String otherTableName();

    /**
     * <li>关联查询一般是主键，但也可以不是主键，所以用此属性指定
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午11:11
     * @描述: 依据此属性指定的字段去对方表中查询
     **/
    String joinColumn() default "id";
}
