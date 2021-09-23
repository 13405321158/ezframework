package com.leesky.ezframework.mybatis.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface JoinColumn {

	/**
	 * <li>1、其值是己方数据表字段名称
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月22日 下午2:10:53
	 *
	 */
	String name() default "";

	/**
	 * <li>1、其值是对方数据表字段名称
	 * @作者: 魏来
	 * @日期: 2021年9月22日 下午2:49:49
	 */
	String referencedColumnName() default "";

	String property() default "";

}
