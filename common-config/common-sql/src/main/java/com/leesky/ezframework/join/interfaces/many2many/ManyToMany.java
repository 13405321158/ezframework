/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */

package com.leesky.ezframework.join.interfaces.many2many;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface ManyToMany {

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:40
	 * @描述: 中间表名称
	 **/
	String middleTableName();// 中间表名称

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月24日 上午10:16:32
	 * @描述: 如果关联关系不是主键，则用此属性指定bean属性
	 * @描述: 此属性=“”代表从表
	 */
	String relationField() default "";

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:11
	 * @描述: 依据此属性指定的字段去中间表中查询
	 * @描述: 关联查询一般是主键，但也可以不是主键，所以用此属性指定
	 **/
	String joinColumn() default "id";

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:40
	 * @描述: 在中间表 中的 字段名称
	 **/
	String middleTableColumn();

	/**
	 * --------------------------------------------------------------------------------------
	 * 
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:33
	 * @描述: 另外一个 many方 数据表名称
	 **/
	String otherManytableName() default "";

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:40
	 * @描述: 另外一个many方 参与关联数据表字段名称
	 **/
	String otherTableColumn() default "";

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/21 上午11:40
	 * @描述: 另外一个many方 在中间表 中的 字段名称
	 **/
	String otherMiddleTableColumn();

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月25日 下午5:21:09
	 * @描述:
	 */
	String order() default "1";

}
