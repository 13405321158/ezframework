package com.leesky.ezframework.auth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.leesky.ezframework.auth.details.userdetails.buyer.BuyerDetailsService;
import com.leesky.ezframework.auth.details.userdetails.saler.SalerDetailsService;
import com.leesky.ezframework.auth.ext.sms.SmsCodeAuthenticationProvider;
import com.leesky.ezframework.auth.ext.webchat.WechatAuthenticationProvider;
import com.leesky.ezframework.backend.api.IbackendServerClient;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String white_name = "/oauth/**";

    private final RedisService cache;
    private final WxMaService wxMaService;
    private final IbackendServerClient client;

    private final BuyerDetailsService buyerDetailsService;
    private final SalerDetailsService salerDetailsService;
    private final UserDetailsService sysUserDetailsService;


    /**
     * 授权控制器访问控制设置
     * <li>/oauth/**开头的url 无需token即可访问，其它url需要授权</li>
     *
     * @author： 魏来
     * @date: 2021/12/1 上午8:45
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers(white_name).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    /**
     * 定义认证管理对象
     *
     * @author： 魏来
     * @date: 2021/12/1 上午8:55
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 定义验证登录用户的方法，这里提供自定义方式入口
     * <li>1、daoAuthenticationProvider 用户名和密码方式，调用loadUserByUsername方法</li>
     * <li>2、微信扫码登录</li>
     * <li>3、sms短信登录</li>
     *
     * @author： 魏来
     * @date: 2021/12/1 上午8:59
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(wechatAuthenticationProvider());
        auth.authenticationProvider(smsCodeAuthenticationProvider());
    }


    /**
     * 用户名密码认证授权提供者
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(sysUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false); // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        return provider;
    }


    /**
     * 手机验证码认证授权提供者: 默认使用salerDetailsService查询用户
     */
    @Bean
    public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider();
        provider.setUserDetailsService(salerDetailsService);
        provider.setCache(cache);
        return provider;
    }

    /**
     * <li>微信认证授权提供者: 默认使用buyerDetailsService查询用户</li>
     * <li>如果salerDetailsService和sysUserDetailsService也想使用微信登录，有两种方案：
     * <p>
     * A、登录用户传递一个标识位，区分卖家、买家和系统用户，则在buyerDetailsService.loadUserByUsername 内根据标识位 使用不同UserDetailsService查询
     * B、不传递标识位，先使用buyerDetailsService.loadUserByUsername查询，然后在使用salerDetailsService和sysUserDetailsService查询
     */
    @Bean
    public WechatAuthenticationProvider wechatAuthenticationProvider() {
        WechatAuthenticationProvider provider = new WechatAuthenticationProvider();
        provider.setUserDetailsService(buyerDetailsService);
        provider.setWxMaService(wxMaService);
        provider.setMemberFeignClient(client);
        return provider;
    }


    /**
     * 密码编码器:根据密码的前缀选择对应的encoder
     * <li>例如：{bcypt}前缀->标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式</li>
     * <li>ps: 源码参见DaoAuthenticationProvider#additionalAuthenticationChecks</li>
     *
     * @author： 魏来
     * @date: 2021/12/1 上午9:07
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
