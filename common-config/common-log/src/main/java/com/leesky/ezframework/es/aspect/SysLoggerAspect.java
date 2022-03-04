package com.leesky.ezframework.es.aspect;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.leesky.ezframework.dto.SysLogDTO;
import com.leesky.ezframework.es.annotation.SysLogAction;
import com.leesky.ezframework.es.annotation.SysLogger;
import com.leesky.ezframework.es.mq.Loginfo2MQService;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.redis.service.RedisService;
import com.leesky.ezframework.utils.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * @author weilai
 * @data 2018年11月21日 下午5:54:15
 * @desc 类描述
 * <li>AOP切面类，主要作用：
 * <li>拦截带有@SysLogger注解修饰的方法，提取方法信息，让后通过Loginfo2MQService发送到MQ消息总线中去
 * <li>用法： 1、需要使用@SysLogger注解的工程引用 common-log工程jar
 * <li>2、在控制器的某个方法上增加：@SysLogger或者@SysLogger(value="任意名称")这个value会写入SysLogModel.java的operation字段中去
 */
@Aspect
@Component
public class SysLoggerAspect {

    @Autowired
    private RedisService cache;

    @Autowired
    private Loginfo2MQService service;

    @Pointcut("@annotation(com.leesky.ezframework.es.annotation.SysLogger)")
    public void loggerPointCut() {

    }

    @Before("loggerPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        SysLogDTO dto = new SysLogDTO();

        String module;
        String className = joinPoint.getTarget().getClass().getName();// 注解所在类名称
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();// 请求的方法名

        SysLogger sysLogger = signature.getMethod().getAnnotation(SysLogger.class);// 注解类

        SysLogAction action = joinPoint.getTarget().getClass().getAnnotation(SysLogAction.class);
        RequestMapping rm = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        if (ObjectUtils.isEmpty(action))
            module = StringUtils.replace(rm.name(), "/", "");
        else
            module = action.name();

        try {
            dto.setUserId(getUserId());
            dto.setUserName(getUserName());
        } catch (Exception e) {
            dto.setUserName("匿名访问");
        }

        dto.setModule(module);
        dto.setAction(sysLogger.action());
        dto.setIp(HttpUtils.getIpAddress());
        dto.setMethod(className + "." + methodName + "()");

        // 请求的参数
        List<String> params = Lists.newArrayList();

        for (Object o : joinPoint.getArgs()) {
            try {
                params.add(JSON.toJSONString(o));
            } catch (Exception e) {
                params.add(o.toString());
            }
        }
        dto.setParams(StringUtils.join(params, ","));

        // 操作日志消息 发送到MQ消息总线上去
        this.service.sendLog(JSON.toJSONString(dto));
    }

    /**
     * 解析JWT获取用户登录名称
     */
    private String getUserName() {

        String token_ = tokenPrefix();
        Object obj = this.cache.get(Common.USER_NAME + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }

    /**
     * 解析JWT获取用户ID
     */
    private String getUserId() {

        String token_ = tokenPrefix();
        Object obj = this.cache.get(Common.USER_ID + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }


    private String tokenPrefix() {
        String token = getToken();
        return StringUtils.split(token, ".")[0];//token的第一部分值
    }

    private String getToken() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Assert.isTrue(ObjectUtils.isNotEmpty(attr), attr.getRequest().getRequestURL() + ": 请求缺少访问令牌");
        return attr.getRequest().getHeader(Common.URL_HEADER_PARAM).replace(Common.TOKEN_TYPE, "");
    }

}
