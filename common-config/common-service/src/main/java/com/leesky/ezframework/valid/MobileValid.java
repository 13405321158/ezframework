/*
 * @作者: 魏来
 * @日期: 2022/1/29 12:42
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 验证手机号码
 */
package com.leesky.ezframework.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MobileValid implements ConstraintValidator<Mobile, String> {

    private static final Pattern pattern = Pattern.compile("^1[3456789]\\d{9}$");

    @Override
    public void initialize(Mobile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext ext) {

        if (StringUtils.isBlank(mobile))
            return false;
        else
            return pattern.matcher(mobile).matches();


    }
}
