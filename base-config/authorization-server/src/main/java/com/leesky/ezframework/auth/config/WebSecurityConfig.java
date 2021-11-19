/**
 * @author: weilai
 * @date: 2018/8/29 17:38
 * @desc: <li>类描述
 * <li>本工程提供授权token生成，所以首先是一个OAuth2认证服务器 @EnableAuthorizationServer(AuthorizationConfig.java)
 * <li>同时还提供角色、用户等权限管理，和数据库打交道，所以还是一个资源服务器 @EnableResourceServer(ResourceServerConfig.java)
 * <li>既然是资源服务器，就需要srpingSecurity进行资源访问权限控制,所以@EnableWebSecurity(当前类)
 * <li>如果不增加@EnableResourceServer注解,会抛出AccessDeniedException 错误
 * <li>
 * <li>
 * <li>ps:
 * <li>WebSecurityConfigurerAdapter是springsecurity的http配置
 * <li>ResourceServerConfigurerAdapter是spring security oauth2的http配置
 * <li>WebSecurityConfigurerAdapter先于ResourceServerConfigurerAdapter起作用,即：
 *
 * <li>ResourceServerConfigurerAdapter配置会覆盖WebSecurityConfigurerAdapter：
 * <li>因为WebSecurityConfigurerAdapter 的order=100
 * <li>而@EnableResourceServer引用了ResourceServerConfiguration,它的order=3
 * <li>在spring 的体系里Order值越小优先级越高
 **/
package com.leesky.ezframework.auth.config;


import com.leesky.ezframework.auth.service.IuserBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // 即权限注解@PreAuthorize("hasRole('Admin')")

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final IuserBaseService userServiceDetail;

    /**
     * @author weilai
     * @desc 用户名和密码方式认证，在AuthorizationConfig.java 中引用
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * <li>: 控制认证服务器中 哪些api访问权限
     *
     * @作者: 魏来
     * @日期: 2021/11/17  下午6:51
     **/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());


//        auth.authenticationProvider(wechatAuthenticationProvider()).
//                authenticationProvider(smsCodeAuthenticationProvider());
    }

    /**
     * 用户名密码认证授权提供者
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceDetail);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false); // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        return provider;
    }

    /**
     * 密码编码器
     * <p>
     * 委托方式，根据密码的前缀选择对应的encoder，例如：{bcypt}前缀->标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式
     * 密码判读 DaoAuthenticationProvider#additionalAuthenticationChecks
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
