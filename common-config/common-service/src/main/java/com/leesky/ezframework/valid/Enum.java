package com.leesky.ezframework.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * <li>枚举校验</li>
 *
 * @author: 魏来
 * @date: 2022/3/12 下午1:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EnmuValid.class) // 绑定对应校验器
public @interface Enum {


    /**
     * 枚举的类型
     */
    Class<?> value();

    /**
     * 错误消息
     */
    String message();

    /**
     * 获取枚举值的方法
     */
    String method() default "getCode";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
