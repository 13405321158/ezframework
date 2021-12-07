/*
 * @作者: 魏来
 * @日期: 2021年12月3日  下午5:54:09
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.security;

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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String AUTHORITY_PREFIX = "ROLE_";

    private final String JWT_AUTHORITIES_KEY = "authorities";

    private final String[] WHITE_URL = new String[]{"/**/public", "/stomp/**", "/v3/api-docs", "/swagger-resources", "/error"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(WHITE_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt()
                ;


    }


}
