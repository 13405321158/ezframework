/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:58:42
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.ext.webchat;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * 类功能说明：
 * <li>微信扫码授权
 */
public class WebchatTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "wechat";
    public final AuthenticationManager authenticationManager;

    public WebchatTokenGranter(AuthorizationServerTokenServices tokenServices, 
    		ClientDetailsService clientDetailsService, 
    		OAuth2RequestFactory requestFactory, 
    		AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

}
