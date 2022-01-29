package com.leesky.ezframework.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ChinaStrValid.class) // 绑定对应校验器
public @interface ChinaStr {

    String message() default "只允许中文字符";

    //将validator进行分类，不同的类group中会执行不同的validator操作
    Class<?>[] groups() default {};

    //主要是针对bean，很少使用
    Class<? extends Payload>[] payload() default {};


}
