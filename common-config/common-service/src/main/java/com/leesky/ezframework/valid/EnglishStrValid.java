/*
 * @作者: 魏来
 * @日期: 2022/1/29 15:48
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 英文字母和数字
 */
package com.leesky.ezframework.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EnglishStrValid implements ConstraintValidator<EnglishStr, String> {

    private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9()]+$");//英文字母和数字


    @Override
    public void initialize(EnglishStr constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s))
            return false;
        else
            return pattern.matcher(s).matches();
    }
}
