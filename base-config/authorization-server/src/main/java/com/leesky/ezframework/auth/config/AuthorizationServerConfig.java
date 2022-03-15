/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午9:28:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: <li>授权中心</li>
 */
package com.leesky.ezframework.auth.config;

import com.google.common.collect.Lists;
import com.leesky.ezframework.auth.details.clientdetails.ClientDetailService;
import com.leesky.ezframework.auth.exception.tokenAuthenticationFilter;
import com.leesky.ezframework.auth.ext.captcha.CaptchaTokenGranter;
import com.leesky.ezframework.auth.ext.sms.SmsCodeTokenGranter;
import com.leesky.ezframework.auth.ext.wx_miniapp.WebchatTokenGranter;
import com.leesky.ezframework.auth.ext.wx_mp.ScanQrTokenGranter;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${access.token.validity:360}") // 默认值过期时间360
    private int accessTokenValiditySeconds;

    @Value("${access.token.validity:420}") // 默认值7分钟
    private int refreshTokenValiditySeconds;

    private final RedisService cache;
    private final JwtEnhancer jwtEnhancer;
    private final AuthenticationManager authManager;// 在WebSecurityConfig中定义了
    private final LettuceConnectionFactory cacheFactory;// token 存储在redis中
    private final ClientDetailService clientDetailsService;// 查找client服务接口
    private final tokenAuthenticationFilter clientDetailFilter;//client认证过滤器

    /**
     * 配置认证异常返回信息
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:28
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.addTokenEndpointAuthenticationFilter(clientDetailFilter);
    }

    /**
     * 查询 登录时查询 client 的有效性 入口方法在此定义
     * <li>对应数据表oauth_client_details</li>
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:28
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 授权服务器 各类参数配置入口
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:28
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        CompositeTokenGranter granters = compositeTokenGranter(endpoints);
        endpoints
                .authenticationManager(authManager)// 用户名和密码验证方式、自定义验证方式(sms、微信等)

                .tokenGranter(granters)// 授权方式结合点. 如果不扩展授权方式,使用系统默认,则此处可以不加

                .reuseRefreshTokens(true)

                .tokenServices(tokenService());// 设置token属性

    }

    /**
     * 设置token 属性
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:27
     */
    @Bean
    public DefaultTokenServices tokenService() {
        DefaultTokenServices services = new DefaultTokenServices();

        services.setReuseRefreshToken(true);//
        services.setSupportRefreshToken(true);// 是否生成刷新token
        services.setTokenStore(tokenStore());// token存储方式：数据表中或缓存(redis)

        services.setClientDetailsService(clientDetailsService);

        services.setAccessTokenValiditySeconds(accessTokenValiditySeconds);// 设置token有效期
        services.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);

        TokenEnhancerChain extJWT = new TokenEnhancerChain();// jwt 内容扩展
        extJWT.setTokenEnhancers(Arrays.asList(jwtTokenConverter(), jwtEnhancer));
        services.setTokenEnhancer(extJWT);

        return services;

    }

    /**
     * 把普通token 转换为jwt
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:27
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return factory.getKeyPair("jwt", "123456".toCharArray());
    }

    /**
     * token 存储到redis中
     *
     * @author： 魏来
     * @date: 2021/12/1 上午9:31
     */
    @Bean
    public RedisTokenStore tokenStore() {
        RedisTokenStore redisStore = new RedisTokenStore(this.cacheFactory);
        redisStore.setPrefix(Redis.AUTH_TOKEN);
        return redisStore;
    }

    /**
     * 扩展授权方式,比如增加:动态图片\微信登录\SMS登录\QQ登录|自定义的二维码登录等
     *
     * @author: 魏来
     * @date: 2022/2/21 上午10:27
     */
    private CompositeTokenGranter compositeTokenGranter(AuthorizationServerEndpointsConfigurer point) {

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> list = Lists.newArrayList(point.getTokenGranter());

        OAuth2RequestFactory factory = point.getOAuth2RequestFactory();
        org.springframework.security.oauth2.provider.ClientDetailsService clientDetails = point.getClientDetailsService();
        AuthorizationServerTokenServices tokenService = point.getTokenServices();

        // 添加图片验证码授权模式授权者
        list.add(new CaptchaTokenGranter(tokenService, clientDetails, factory, authManager, cache));
        // 添加手机短信验证码授权模式的授权者
        list.add(new SmsCodeTokenGranter(tokenService, clientDetails, factory, authManager));
        // 添加微信小程序登录(一键登录)授权模式的授权者
        list.add(new WebchatTokenGranter(tokenService, clientDetails, factory, authManager));
        //添加微信扫码登录(pc网页扫码登录)
        list.add(new ScanQrTokenGranter(tokenService, clientDetails, factory, authManager));

        return new CompositeTokenGranter(list);
    }


}
