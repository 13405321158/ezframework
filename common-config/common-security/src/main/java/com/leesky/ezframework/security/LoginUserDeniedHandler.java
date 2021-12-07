package com.leesky.ezframework.security;

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
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/7 下午12:54
 */
@Slf4j
@Configuration
public class LoginUserDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("已经登录状态异常拦截：{}", request.getRequestURI());
        Map<String, Object> map = new HashMap<>();
        map.put("code", response.SC_UNAUTHORIZED);//
        map.put("success", false);
        map.put("msg", accessDeniedException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("tips", "Sentury Tire Co., Ltd");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);
    }
}
