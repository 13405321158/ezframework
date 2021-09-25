package com.leesky.ezframework.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.leesky.ezframework.mybatis.enums.FetchType;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {

	/**
	 * <li>one2many数据加载类型</li>
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午10:06:02
	 */
	FetchType fetch() default FetchType.LAZY;

}
