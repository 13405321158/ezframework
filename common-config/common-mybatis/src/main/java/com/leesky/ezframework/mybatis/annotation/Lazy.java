package com.leesky.ezframework.mybatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <li>@Lazy =懒加载</li>
 * <li>@Lazy(true) =懒加载</li>
 * <li>@Lazy(value=true) =懒加载</li>
 * <li>@Lazy(false) =加载关系</li>
 * <li>使用了@Lazy 后 @AutoLazy注解失效</li>
 *
 * @作者: 魏来
 * @日期: 2021/9/17  下午2:31
 **/
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Lazy {
    boolean value() default true;
}