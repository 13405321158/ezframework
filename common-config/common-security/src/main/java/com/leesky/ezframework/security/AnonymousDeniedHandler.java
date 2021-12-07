package com.leesky.ezframework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/7 下午12:51
 */
@Slf4j
@Configuration
public class AnonymousDeniedHandler implements AuthenticationEntryPoint, InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("未登录状态异常拦截：{}", request.getRequestURI());
        String msg = authException.getMessage();


        if (authException instanceof BadCredentialsException)
            msg = "密码错误!";

        if (authException instanceof UsernameNotFoundException)
            msg = "用户不存在";

        if (authException instanceof InvalidBearerTokenException)
            msg = "token无效,认证失败";

        Map<String, Object> map = new HashMap<>();
        map.put("code", response.SC_UNAUTHORIZED);//
        map.put("success", false);
        map.put("msg", msg);
        map.put("ex", authException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("tips", "Sentury Tire Co., Ltd");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);
    }
}
