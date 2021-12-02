/*
 * @作者: 魏来
 * @日期: 2021年12月2日  下午3:34:37
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 类功能说明：
 * <li>自定义过滤器</li>
 */
public class TokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

	private AuthorizationServerSecurityConfigurer configurer;
	private AuthenticationEntryPoint authenticationEntryPoint;

	public TokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer) {
		this.configurer = configurer;
	}

	@Override
	public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		// 把父类的干掉
		super.setAuthenticationEntryPoint(null);
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Override
	protected AuthenticationManager getAuthenticationManager() {
		return configurer.and().getSharedObject(AuthenticationManager.class);
	}

	@Override
	public void afterPropertiesSet() {
		setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
				authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
			}
		});
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

			}
		});
	}

}
