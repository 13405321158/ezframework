/*
 * @作者: 魏来
 * @描述:
 * OAuth2 resource server support

只要类路径上有spring-security-oauth2-resource-server模块，并且设置了JWK Set URI，Spring Boot就可以设置OAuth2资源服务器。例如：

spring.security.oauth2.resource.jwt.jwk.set-uri =https://example.com/oauth2/default/v1/keys
 */
package com.leesky.ezframework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;


/**
 * 类功能说明：
 * <li>资源服务器配置</li>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.brac:false}")
    private Boolean config;

    private final CustomAccessDeineHandler accessHandler;
    private final CustomAuthenticationEntryPoint entryPoint;

    private final String[] WHITE_URL = new String[]{"/**/public", "/stomp/**", "/v3/api-docs", "/swagger-resources", "/error"};

    /**
     * 以下内容已经经过测试，配置正确，请勿随意修改
     * JwtDecoder需要的公钥配置(jwkSetUri)已经在yml中设置
     *
     * @author： 魏来
     * @date: 2021/12/8 上午10:30
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        config(http);

        //以下内容已经经过测试，配置正确，请勿随意修改
        http.oauth2ResourceServer()
                .authenticationEntryPoint(entryPoint)//处理未认证
                .accessDeniedHandler(accessHandler);// 处理未授权

        http.exceptionHandling()
                .accessDeniedHandler(accessHandler) // 处理未授权
                .authenticationEntryPoint(entryPoint); //处理未认证

        http.csrf().disable();
    }

    /**
     * 是否启用 基于角色控制
     *
     * @author： 魏来
     * @date: 2021/12/15 上午9:08
     */
    private void config(HttpSecurity http) throws Exception {
        if (config) {
            http.authorizeRequests()
                    .antMatchers(WHITE_URL).permitAll()
                    .anyRequest().access("@roleChecker.check(authentication,request)")
                    .and()
                    .oauth2ResourceServer().jwt();
        } else {
            http.authorizeRequests()
                    .antMatchers(WHITE_URL).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt();
        }
    }

}
