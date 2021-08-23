/*
 * @author:weilai
 * @Data:2020-8-1910:08:48
 * @Org:Sentury Co., ltd.
 * @Department:Domestic Sales, Tech Center
 * @Desc: <li>
 */

package com.leesky.ezframework.join.interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface OneToOne {

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
	 * @描述: 依据此属性指定的字段去 对方表中查询
	 * @描述: 关联查询一般是主键，但也可以不是主键，所以用此属性指定
	 **/
	String joinColumn() default "id";

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月23日 下午5:18:15
	 * @描述: 数据表对应的service名称：ixxxxService
	 */
	String serviceName();
}
