package com.leesky.ezframework.mybatis.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE})
@Retention(RUNTIME)
public @interface DisableAutoMapper {
	boolean value() default true;
}