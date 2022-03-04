package com.leesky.ezframework.es.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ver: 1.0.0
 * @Data:下午2:24:17 , 2019年11月16日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>自定义注解类：用在控制器到方法上，收集使用信息，并保存到mongodb中
 * <li>使用方法：在方法上增加 @SysLogger("方法名称")
 * <li>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLogger {
    /**
     * <li>操作动作</li>
     *
     * @author: 魏来
     * @date: 2022/3/4 下午1:12
     */
    String action();
}
