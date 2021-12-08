package com.leesky.ezframework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 　　AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常   ——  也就是未授权的问题
 *
 * 　　AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常  ——  也就是权限不足的问题
 *
 * @author： 魏来
 * @date： 2021/12/7 下午12:54
 */
@Slf4j
@Configuration
public class CustomAccessDeineHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("AccessDeniedHandler状态异常拦截：{}", request.getRequestURI());

        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("code", response.getStatus());
        map.put("path", request.getServletPath());
        map.put("msg", accessDeniedException.getMessage());
        map.put("timestamp", LocalDateTime.now().toString());

        response.setContentType("application/json");
        response.setStatus(response.getStatus());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);
    }
}
