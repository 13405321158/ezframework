/*
 * @作者: 魏来
 * @描述:
 * OAuth2 resource server support

只要类路径上有spring-security-oauth2-resource-server模块，并且设置了JWK Set URI，Spring Boot就可以设置OAuth2资源服务器。例如：

spring.security.oauth2.resource.jwt.jwk.set-uri =https://example.com/oauth2/default/v1/keys
 */
package com.leesky.ezframework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


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

    private final LettuceConnectionFactory redisConnectionFactory;// token 存储在redis中
    /**
     * 以下内容已经经过测试，配置正确，请勿随意修改
     * JwtDecoder需要的公钥配置(jwkSetUri)已经在yml中设置
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


    /**
     * token 存储到redis中
     *
     * @author： 魏来
     * @date: 2021/12/1 上午9:31
     */
    @Bean
    public RedisTokenStore tokenStore() {
        RedisTokenStore redisStore = new RedisTokenStore(redisConnectionFactory);
        redisStore.setPrefix("auth-token:");
        return redisStore;
    }
}
