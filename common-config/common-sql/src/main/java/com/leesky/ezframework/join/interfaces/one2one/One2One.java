/*
 * @author:weilai
 * @Data:2020-8-1910:08:48
 * @Org:Sentury Co., ltd.
 * @Department:Domestic Sales, Tech Center
 * @Desc: <li>
 */

package com.leesky.ezframework.join.interfaces.one2one;

import java.lang.annotation.*;

/**
 * 一对一映射
 * 作者: 魏来
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface One2One {

    /**
     * <li> 实体类表名称
     * <li> 另一个one方的数据表名称，注意是另一个one方，千万别写成当前one方的数据表名称
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午10:46
     **/
    String otherOneTableName();

    /**
     * <li> 依据此属性指定的字段去对方表中查询
     * <li> 关联查询一般是主键，但也可以不是主键，所以用此属性指定
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午10:46
     **/
    String otherOneTableColumn() default "id";

    /**
     * <li> 标记映射关系 和当前bean哪个属性 关联
     * <li> one2one 关系可以划分为主表 和 从表： 主表含有从表字段， 从表不含有主表字段
     * <li> 此属性=“”代表从表;
     *
     * @作者: 魏来
     * @日期: 2021/8/21 上午10:46
     */
    String joinField() default "";

}
