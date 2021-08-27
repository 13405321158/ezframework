/*
 * @author:weilai
 * @Data:2020-8-1910:08:48
 * @Org:Sentury Co., ltd.
 * @Department:Domestic Sales, Tech Center
 * @Desc: <li>
 */

package com.leesky.ezframework.join.interfaces.one2one;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface One2One {

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午10:46
	 * @描述: 实体类表名称
	 * @描述: 另一个one方的数据表名称，注意是另一个one方，千万别写成当前one方的数据表名称
	 **/
	String otherOneTableName();

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:11
	 * @描述: 依据此属性指定的字段去对方表中查询
	 * @描述: 关联查询一般是主键，但也可以不是主键，所以用此属性指定
	 **/
	String joinColumn() default "id";

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月24日 上午10:16:32
	 * @描述: one2one 关系可以划分为主表 和 从表： 主表含有从表字段， 从表不含有主表字段
	 * @描述: 此属性=“”代表从表
	 */
	String relationField() default "";

}
