package com.leesky.ezframework.utils;


import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;

/**
 * 资源服务器工具类：获取登录用户Id、username和角色列表
 *
 * @author： 魏来
 * @date： 2021/12/8 下午6:29
 */
@Service
public class JwtUtils {

    private final static String TOKEN_TYPE = "bearer ";//接口认证方式
    private final static String ROLE_LIST = "authorities";//jwt 中用户权限部分
    private final static String URL_HEADER_PARAM = "Authorization";//http请求时头部参数名

    @Autowired
    private RedisTokenStore redisTokenStore;


    /**
     * 解析JWT获取用户登录名称
     */
    public String getUserName() {
        Map<String, Object> ext = (Map<String, Object>) getExtInfo().get("user_info");

        return (String) ext.get("username");
    }

    /**
     * 解析JWT获取用户ID
     */
    public String getUserId() {
        Map<String, Object> ext = (Map<String, Object>) getExtInfo().get("user_info");

        return (String) ext.get("userId");
    }

    /**
     * JWT获取用户角色列表
     */
    @SneakyThrows
    public List<String> getRoles() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attr.getRequest().getHeader(URL_HEADER_PARAM);

        SignedJWT jwt = SignedJWT.parse(token.replace(TOKEN_TYPE, ""));
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        JSONArray array = (JSONArray) claims.getClaim(ROLE_LIST);
        return JSON.parseArray(array.toJSONString(), String.class);
    }


    private Map<String, Object> getExtInfo() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attr.getRequest().getHeader(URL_HEADER_PARAM).replace(TOKEN_TYPE, "");
        OAuth2AccessToken accessToken = redisTokenStore.readAccessToken(token);
        return accessToken.getAdditionalInformation();

    }
}
