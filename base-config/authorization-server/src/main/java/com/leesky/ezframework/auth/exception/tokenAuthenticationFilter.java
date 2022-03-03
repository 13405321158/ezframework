/*
 * @作者: 魏来
 * @日期: 2022/3/1 上午10:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.auth.details.clientdetails.ClientDetailService;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.utils.Base64codeUtil;
import com.leesky.ezframework.utils.I18nUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <li>自定义一个OncePerRequestFilter子类，在filter中重写认证逻辑，再将其注入到AuthorizationServerSecurityConfigurer</li>
 * <li>用于 请求头Authorization=Basic+" "+BASE64方式加密(client_id:client_secret) 方式的错误捕获</li>
 * <li>密码模式获取token时需要先验证客户端、再验证用户</li>
 *
 * @author: 魏来
 * @date: 2022/3/1 上午10:12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class tokenAuthenticationFilter extends OncePerRequestFilter {

    private final I18nUtil i18nUtil;
    private final PasswordEncoder passwordEncoder;
    private final ClientDetailService clientDetailsService;

    /**
     * <li>过滤器只拦截：/oauth/token，其它URL放行</li>
     *
     * @author: 魏来
     * @date: 2022/3/1 上午11:00
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果不是获取token的URL，则直接放行
        if (!request.getRequestURI().contains("/oauth/token")) {
            filterChain.doFilter(request, response);
            return;
        }


        String[] clientDetails = this.getClientIdAndSecret(request, response);

        this.handle(request, response, clientDetails, filterChain);
    }

    /**
     * 当前系统一个username 有两个clientId：1、client_id=username; 2、client_id= mobile;
     * 对应client_secret分别为： username+_pwD@?123；mobile+_pwD@?123，目的适应使用用户名 和 手机号登录方式
     *
     * @author: 魏来
     * @date: 2022/3/1 下午4:41
     */
    private void handle(HttpServletRequest request, HttpServletResponse response, String[] clientDetails, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = commonCode(request, response);
        String msg = this.i18nUtil.getMsg("login.client.clientId.clientSecret");
        try {
            ClientDetails details = this.clientDetailsService.loadClientByClientId(clientDetails[0]);

            boolean matches = this.passwordEncoder.matches(clientDetails[1], details.getClientSecret());
            if (matches) {//如果client_secret 密码不匹配，则直接输出错误信息
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details.getClientId(), clientDetails[1], details.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
                filterChain.doFilter(request, response);
                return;//通过验证直接返回
            }
        } catch (Exception e) {//查询不到客户端，捕获错误信息
            msg = e.getMessage();
        }
        log.error(msg);
        map.put("msg", msg);
        mapper.writeValue(response.getOutputStream(), map);
    }


    /**
     * 1、获取client_id和client_secret： 适应两种方式： 1、请求头参数：Authorization： Basic BASE64(client_id:client_secret); 2、form方式提交参数client_id和client_secret
     * 2、数据库表表存储的clent_secret 实际是 username+_pwD@?123 或者 mobile+_pwD@?123 的加密字符串
     * 3、(重点)前端传递的参数要求：client_id=username或者mobile；client_secret=client_id+年月日
     */
    private String[] getClientIdAndSecret(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg00 = this.i18nUtil.getMsg("login.client.param.null");
        String msg01 = this.i18nUtil.getMsg("login.client.authorization.error");
        ObjectMapper mapper = new ObjectMapper();
        String basic = request.getHeader(Common.URL_HEADER_PARAM);
        Map<String, Object> map = commonCode(request, response);

        //1、请求头参数方式
        if (StringUtils.isNotBlank(basic) && basic.startsWith(Common.BASIC)) {
            basic = basic.replace(Common.BASIC, Strings.EMPTY).trim();
            String basicPlainText = Base64codeUtil.Base642String(basic);
            String[] params = StringUtils.split(basicPlainText, ":");
            if (params.length != 2) {//如果不是 client_id:client_secret
                log.error(msg01);
                map.put("msg", msg01);
                mapper.writeValue(response.getOutputStream(), map);
                return null;
            }

            return params;
        }

        //2、form表单 提交参数方式
        String id = request.getParameter("client_id");
        String secret = request.getParameter("client_secret");

        if (StringUtils.isBlank(id) || StringUtils.isBlank(secret)) {
            log.error(msg00);
            map.put("msg", msg00);
            mapper.writeValue(response.getOutputStream(), map);
            return null;
        }

        return new String[]{id, secret};

    }

    private Map<String, Object> commonCode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> map = Maps.newHashMap();
        map.put("success", false);
        map.put("code", response.getStatus());
        map.put("path", request.getServletPath());
        map.put("timestamp", LocalDateTime.now().toString());

        return map;
    }
}
