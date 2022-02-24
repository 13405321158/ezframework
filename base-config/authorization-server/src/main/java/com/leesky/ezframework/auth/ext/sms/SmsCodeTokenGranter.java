/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:54:35
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.ext.sms;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 类功能说明：
 * <li>sms方式授权
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

    public static final String GRANT_TYPE = "sms_code";
    public final AuthenticationManager authenticationManager;

    public SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory,
                               AuthenticationManager authenticationManager) {

        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;

    }

    @Override
    @SneakyThrows(InvalidGrantException.class)
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = Maps.newHashMap(tokenRequest.getRequestParameters());

        String code = parameters.get("code"); // 短信验证码
        String mobile = parameters.get("mobile"); // 手机号

        parameters.remove("code");

        OAuth2Request storedOAuth2Request = null;

        Authentication userAuth = new SmsCodeAuthenticationToken(mobile, code);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);


        userAuth = this.authenticationManager.authenticate(userAuth);


        if (userAuth.isAuthenticated())
            storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);


        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
