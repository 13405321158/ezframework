package com.leesky.ezframework.auth.config;

import com.leesky.ezframework.auth.details.userdetails.sys.SysUserDetailsService;
import com.leesky.ezframework.auth.ext.sms.SmsCodeAuthenticationProvider;
import com.leesky.ezframework.auth.ext.wx_miniapp.WechatAuthenticationProvider;
import com.leesky.ezframework.auth.ext.wx_mp.ScanQrAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final WechatAuthenticationProvider wxProvider;
    private final SmsCodeAuthenticationProvider smsProvider;
    private final ScanQrAuthenticationProvider wxscanProvider;

    private final SysUserDetailsService sysUserDetailsService;

    /**
     * 授权控制器访问控制设置
     * <li>/oauth/**开头的url 无需token即可访问，其它url需要授权</li>
     *
     * @author： 魏来
     * @date: 2021/12/1 上午8:45
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String white_name = "/oauth/**";
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
        auth.authenticationProvider(this.wxProvider);//微信认证授权提供者: 默认使用buyerDetailsService查询用户
        auth.authenticationProvider(this.smsProvider);//手机验证码认证授权提供者: 默认使用salerDetailsService查询用户
        auth.authenticationProvider(this.wxscanProvider);//微信扫码(pc端网页)
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    /**
     * 默认：用户名密码认证授权提供者
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
