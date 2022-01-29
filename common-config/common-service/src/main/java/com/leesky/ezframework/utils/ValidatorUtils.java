/*
 * @作者: 魏来
 * @日期: 2022/1/29 10:23
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 参数校验工具类
 */
package com.leesky.ezframework.utils;


import com.google.common.collect.Lists;
import com.leesky.ezframework.exception.ApiException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     */
    public static void valid(Object object, Class<?>... groups) {
        List<String> error = Lists.newArrayList();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            constraintViolations.forEach(o -> error.add(o.getPropertyPath() + ":" + o.getMessage()));
            throw new ApiException(StringUtils.join(error, ","));
        }
    }


}
