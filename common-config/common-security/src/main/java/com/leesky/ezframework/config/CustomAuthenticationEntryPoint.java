/*
AuthenticationEntryPoint提供了一些内置实现：

1、Http403ForbiddenEntryPoint：设置响应状态字为403，并非触发一个真正的认证流程。通常在一个预验证(pre-authenticated authentication)已经得出结论需要拒绝用户请求的情况被用于拒绝用户请求。

2、HttpStatusEntryPoint：设置特定的响应状态字，并非触发一个真正的认证流程。

3、LoginUrlAuthenticationEntryPoint：根据配置计算出登录页面url，将用户重定向到该登录页面从而开始一个认证流程。

4、BasicAuthenticationEntryPoint：对应标准Http Basic认证流程的触发动作，向响应写入状态字401和头部WWW-Authenticate:"Basic realm="xxx"触发标准Http Basic认证流程。

5、DigestAuthenticationEntryPoint：对应标准Http Digest认证流程的触发动作，向响应写入状态字401和头部WWW-Authenticate:"Digest realm="xxx"触发标准Http Digest认证流程。

6、DelegatingAuthenticationEntryPoint：这是一个代理，将认证任务委托给所代理的多个AuthenticationEntryPoint对象，其中一个被标记为缺省AuthenticationEntryPoint。

7、 AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常   ——  也就是未授权的问题
    AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常  ——  也就是权限不足的问题
 */
package com.leesky.ezframework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
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
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("AuthenticationEntryPoint状态异常拦截：{}", request.getRequestURI());

        Map<String, Object> map = new HashMap<>();
        map.put("code", response.SC_UNAUTHORIZED);//
        map.put("success", false);
        map.put("msg", authException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", LocalDateTime.now().toString());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);
    }
}
