/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:33:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
     * @see org.springframework.security.oauth2.provider.CompositeTokenGranter#grant(String, TokenRequest)
     * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#grant(String, TokenRequest)
 */
package com.leesky.ezframework.auth.ext.captcha;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * 类功能说明：
 * <li>动态图形验证码授权
 * <li>声明授权者 CaptchaTokenGranter 支持授权模式 captcha
 * <li>根据接口传值 grant_type = captcha的值匹配到此授权者
 * 
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "captcha";
	
//    private StringRedisTemplate redisTemplate;
//	private final AuthenticationManager authenticationManager;

	public CaptchaTokenGranter(AuthorizationServerTokenServices tokenServices, 
			ClientDetailsService clientDetailsService, 
			OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager
			//StringRedisTemplate redisTemplate
			) {
		
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		
//		this.redisTemplate = redisTemplate;
//		this.authenticationManager = authenticationManager;

	}
	
	
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		return null;
    	
    	
    	
    }

}
