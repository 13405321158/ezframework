package com.leesky.ezframework.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EnglishStrValid.class) // 绑定对应校验器
public @interface EnglishStr {


    String message() default "只允许字母和数字";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
