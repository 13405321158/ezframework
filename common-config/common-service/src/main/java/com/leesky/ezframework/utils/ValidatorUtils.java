/*
 * @作者: 魏来
 * @日期: 2022/1/29 10:23
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 参数校验工具类
 */
package com.leesky.ezframework.utils;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * object 整体校验，抛出全部错误信息
     */
    public static void all(Object object, Class<?>... groups) {
        List<String> error = Lists.newArrayList();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            constraintViolations.forEach(o -> error.add(o.getPropertyPath() + ":" + o.getMessage()));
            throw new IllegalArgumentException(StringUtils.join(error, ","));
        }
    }

    /**
     * object 整体校验，抛出第一个错误信息
     */
    public static void first(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);

        constraintViolations.forEach(e -> {
            throw new IllegalArgumentException(e.getPropertyPath() + ":" + e.getMessage());
        });
    }

    /**
     * object 校验指定的单个属性
     */
    public static void valid(Object object, String property, Class<?>... groups) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(object, property, groups);

        constraintViolations.forEach(e -> {
            throw new IllegalArgumentException(e.getPropertyPath() + ":" + e.getMessage());
        });
    }

    /**
     * object 校验指定的多个属性
     */
    public static void valid(Object object, List<String> property, Class<?>... groups) {
        List<String> error = Lists.newArrayList();
        Set<ConstraintViolation<Object>> constraintViolations = Sets.newHashSet();
        property.forEach(e -> {
            Set<ConstraintViolation<Object>> c = validator.validateProperty(object, e, groups);
            constraintViolations.addAll(c);
        });


        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            constraintViolations.forEach(o -> error.add(o.getPropertyPath() + ":" + o.getMessage()));
            throw new IllegalArgumentException(StringUtils.join(error, ","));
        }
    }

    /**
     * object 校验指定的单个属性
     */
    public static <T> Set<ConstraintViolation<T>> valid(Class<T> object, String property, Object value, Class<?>... groups) {

        Set<ConstraintViolation<T>> constraintViolations = validator.validateValue(object, property, value, groups);

        constraintViolations.forEach(e -> {
            throw new IllegalArgumentException(e.getPropertyPath() + ":" + e.getMessage());
        });

        return null;
    }

}
