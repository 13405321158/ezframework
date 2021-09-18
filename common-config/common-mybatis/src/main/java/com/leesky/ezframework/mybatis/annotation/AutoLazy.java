package com.leesky.ezframework.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 说明：
 * <li>如属性使用了@Lazy 则@AutoLazy自动失效
 * <li>@AutoLazy =懒加载</li>
 * <li>@AutoLazy(false) =懒加载</li>
 * <li>@AutoLazy(value =false) =懒加载</li>
 * <li>@AutoLazy(true) =自动加载,懒加载失效</li>
 *
 * @作者: 魏来
 * @日期: 2021/9/17  下午2:38
 **/
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface AutoLazy {
    boolean value() default false;
}