package com.leesky.ezframework.mybatis.ddl.annotation;

import java.lang.annotation.*;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/26 上午8:23
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeDDL {
    boolean isExclude() default true;
}
