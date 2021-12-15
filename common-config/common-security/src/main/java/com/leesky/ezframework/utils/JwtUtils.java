package com.leesky.ezframework.utils;


import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.redis.service.RedisService;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 资源服务器工具类：获取登录用户Id、username和角色列表
 *
 * @author： 魏来
 * @date： 2021/12/8 下午6:29
 */
@Service
public class JwtUtils {


    @Autowired
    private RedisService redisService;

    /**
     * 解析JWT获取用户登录名称
     */
    public String getUserName() {

        String token_ = tokenPrefix();
        Object obj = this.redisService.get(Common.USER_NAME + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }

    /**
     * 解析JWT获取用户ID
     */
    public String getUserId() {

        String token_ = tokenPrefix();
        Object obj = this.redisService.get(Common.USER_ID + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }

    /**
     * 解析JWT获取用户角色列表
     */
    @SneakyThrows
    public List<String> getRoles() {
        String token = getToken();
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        JSONArray array = (JSONArray) claims.getClaim(Common.ROLE_LIST);
        return JSON.parseArray(array.toJSONString(), String.class);
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
