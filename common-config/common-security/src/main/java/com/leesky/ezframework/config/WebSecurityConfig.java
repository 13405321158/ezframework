/*
 * @作者: 魏来
 * @日期: 2021年12月3日  下午5:54:09
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 类功能说明：
 * <li>资源服务器配置</li>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String AUTHORITY_PREFIX = "ROLE_";

    private final String JWT_AUTHORITIES_KEY = "authorities";

    private final String[] WHITE_URL = new String[]{"/**/public", "/stomp/**", "/v3/api-docs", "/swagger-resources", "/error"};

    private final CustomAccessDeineHandler accessHandler;
    private final CustomAuthenticationEntryPoint entryPoint;


    /**
     * 以下内容已经经过测试，配置正确，请勿随意修改
     *
     * @author： 魏来
     * @date: 2021/12/8 上午10:30
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(WHITE_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();

        //以下内容已经经过测试，配置正确，请勿随意修改
        http.oauth2ResourceServer()
                .authenticationEntryPoint(entryPoint)//处理未认证
                .accessDeniedHandler(accessHandler);// 处理未授权

        http.exceptionHandling()
                .accessDeniedHandler(accessHandler) // 处理未授权
                .authenticationEntryPoint(entryPoint); //处理未认证
    }

}
