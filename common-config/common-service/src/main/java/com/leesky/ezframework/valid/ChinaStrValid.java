/*
 * @作者: 魏来
 * @日期: 2022/1/29 15:00
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 中文字符串验证：只允许中文字符和().  因为中国的公司名称可以含有小括弧
 */
package com.leesky.ezframework.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ChinaStrValid implements ConstraintValidator<ChinaStr, String> {

    private static final Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5A-Za-z0-9()]+$");//中文字符及()


    @Override
    public void initialize(ChinaStr constraintAnnotation) {
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
