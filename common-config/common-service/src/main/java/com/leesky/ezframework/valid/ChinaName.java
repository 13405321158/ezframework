/*
 * @作者: 魏来
 * @日期: 2022/1/29 12:41
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 中文名称验证注解
 */
package com.leesky.ezframework.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ChinaNameValid.class) // 绑定对应校验器
public @interface ChinaName {

    String message() default "【姓名、昵称】必须是中文字符";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
