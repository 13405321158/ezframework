package com.leesky.ezframework.es.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <li>标注在控制器上，定义控制器名称</li>
 *
 * @author: 魏来
 * @date: 2022/3/4 下午12:48
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLogAction {

    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";

}
