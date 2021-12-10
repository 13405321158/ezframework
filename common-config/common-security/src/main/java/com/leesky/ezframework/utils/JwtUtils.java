package com.leesky.ezframework.utils;


import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/8 下午6:29
 */
public class JwtUtils {

    private final static String TOKEN_TYPE = "bearer";//接口认证方式
    private final static String ROLE_LIST = "authorities";//jwt 中用户权限部分
    private final static String URL_HEADER_PARAM = "Authorization";//http请求时头部参数名


    /**
     * JWT获取用户角色列表
     */
    @SneakyThrows
    public static List<String> getRoles() {
        SignedJWT jwt = getJwtPayload();
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        JSONArray array = (JSONArray) claims.getClaim(ROLE_LIST);
        return JSON.parseArray(array.toJSONString(), String.class);
    }



    @SneakyThrows
    private static SignedJWT getJwtPayload() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attr.getRequest().getHeader(URL_HEADER_PARAM);
        return SignedJWT.parse(token.replace(TOKEN_TYPE, ""));
    }

//    /**
//     * 解析JWT获取用户ID
//     *
//     * @return
//     */
//    public static Long getUserId() {
//        Long id = getJwtPayload().getLong(SecurityConstants.USER_ID_KEY);
//        return id;
//    }
//
//    /**
//     * 解析JWT获取获取用户名
//     *
//     * @return
//     */
//    public static String getUsername() {
//        String username = getJwtPayload().getStr(SecurityConstants.USER_NAME_KEY);
//        return username;
//    }
//
//
//

}
