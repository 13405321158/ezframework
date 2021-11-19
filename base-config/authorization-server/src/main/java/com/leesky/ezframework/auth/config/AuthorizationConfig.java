/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:11
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * <li>描述: 认证服务器配置类
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${access.token.validity:360}") // 默认值过期时间360
    private int accessTokenValiditySeconds;

    @Value("${access.refresh.validity:420}") // 默认值7分钟
    private int refreshTokenValiditySeconds;


    private final   DataSource dataSource;

    private final ExceptionTranslator exceptionTranslator;

    private final AuthenticationManager authenticationManager;

    /**
     * @Auther: weilai
     * @Date: 2018/10/28 17:24
     * @Description: <li>1、配置tokenStore</li>
     * <li>2、声明加密方式使用AuthenticationManager</li>
     * <li>3、用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token
     * services)。</li>
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        /**将增强的token设置到增强链中*/
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenConverter(), new JwtEnhance()));

        /**配置TokenServices参数*/
        DefaultTokenServices services = new DefaultTokenServices();
        /**刷新token是否生成*/
        services.setSupportRefreshToken(true);
        /**token存储方式：jdbc或token*/
        services.setTokenStore(tokenStore());
        /**jwt扩展*/
        services.setTokenEnhancer(tokenEnhancerChain);
        /**设置token有效期*/
        services.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        services.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        /**用户名和密码验证方式*/
        endpoints.tokenServices(services).authenticationManager(authenticationManager);
        /**认证通过后的异常信息捕获*/
        endpoints.exceptionTranslator(exceptionTranslator);

    }

    /**
     * @author:weilai
     * @Data:2020年5月7日 上午9:39:43
     * @Desc: <li>把普通token 转换为jwt
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("leesky.jks"), "pwd123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("keyPair"));
        return converter;
    }

    @Bean
    public JdbcTokenStores tokenStore() {
        return new JdbcTokenStores(dataSource);
    }
}
