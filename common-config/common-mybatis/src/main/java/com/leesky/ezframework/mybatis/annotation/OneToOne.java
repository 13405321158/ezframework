package com.leesky.ezframework.mybatis.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.leesky.ezframework.mybatis.enums.FetchType;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface OneToOne {

	/**
	 * <li>one2one数据加载类型</li>
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午10:06:02
	 */
	FetchType fetch() default FetchType.EAGER;

}
