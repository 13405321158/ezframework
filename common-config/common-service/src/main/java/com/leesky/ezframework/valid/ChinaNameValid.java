/*
 * @作者: 魏来
 * @日期: 2022/1/29 13:21
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ChinaNameValid implements ConstraintValidator<ChinaName, String> {

    private static final Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]{2,4}$");//2-4个中文字符及(),长度3~7


    @Override
    public void initialize(ChinaName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {

        if (StringUtils.isBlank(name))
            return false;
        else
            return pattern.matcher(name).matches();
    }
}
