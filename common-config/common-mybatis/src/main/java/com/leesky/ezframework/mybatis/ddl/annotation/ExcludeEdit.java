package com.leesky.ezframework.mybatis.ddl.annotation;

import java.lang.annotation.*;

/**
 * <li>带有此注解时，无论此字段发生任何变化都忽略编辑，需要手工处理</li>
 *
 * @author: 魏来
 * @date: 2022/2/26 上午8:23
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeEdit {
    boolean isExclude() default true;
}
