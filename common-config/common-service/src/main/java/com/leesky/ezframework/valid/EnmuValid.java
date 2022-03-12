/*
 * @作者: 魏来
 * @日期: 2022/3/12 下午1:02
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.valid;

import com.google.common.collect.Lists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/12 下午1:02
 */
public class EnmuValid implements ConstraintValidator<Enum, String> {


    /**
     * 枚举的值
     */
    private final List<String> values = Lists.newArrayList();

    @Override
    public void initialize(Enum constraintAnnotation) {

        Class<?> enumClazz = constraintAnnotation.value();
        Object[] enumConstants = enumClazz.getEnumConstants();
        if (null == enumConstants)
            return;
        
        Method method;
        try {
            method = enumClazz.getMethod(constraintAnnotation.method());
        } catch (Exception e) {
            throw new RuntimeException("枚举未找到方法" + constraintAnnotation.method(), e);
        }

        method.setAccessible(true);
        try {
            for (Object enumConstant : enumConstants) {
                values.add(method.invoke(enumConstant).toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("获取枚举值失败", e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return null == value || values.contains(value);
    }


}
