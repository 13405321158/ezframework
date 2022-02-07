/*
 * @作者: 魏来
 * @日期: 2022/1/29 14:04
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.valid;

import com.leesky.ezframework.utils.IdCardUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValid implements ConstraintValidator<IdCard, String> {


    @Override
    public void initialize(IdCard constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ext) {
        boolean ret = false;

        if (StringUtils.isBlank(s))
            return false;

        try {
            IdCardUtil.isValidatedAllIdcard(s);
            ret = true;
        } catch (Exception e) {
           ext.disableDefaultConstraintViolation();
            ext.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
        }



        return ret;
    }
}
