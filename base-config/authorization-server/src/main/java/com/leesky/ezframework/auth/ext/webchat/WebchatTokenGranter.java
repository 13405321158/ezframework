/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:58:42
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  声明授权者 CaptchaTokenGranter 支持授权模式 wechat
 * 根据接口传值 grant_type = wechat 的值匹配到此授权者
 * 匹配逻辑详见下面的两个方法
 *
 * @see org.springframework.security.oauth2.provider.CompositeTokenGranter#grant(String, TokenRequest)
 * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#grant(String, TokenRequest)
 */
package com.leesky.ezframework.auth.ext.webchat;

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
 * <li>各类微信小程序登录，比如 麒麟保、小程序商城</li>
 * <li>步骤：</li>
 * 1、小程序 通过js 访问微信 ，返回 iv、code、encryptedData、rawData、signature
 * 2、小程序 访问我方登录接口，其中grant_type=webchat(其它三个form表单参数是 iv、code、encryptedData)则会进入当前类getOAuth2Authentication()方法中
 * 3、本认证过程 是根据 iv、code、encryptedData 获取微信用户的 各类信息如：昵称，手机号码等
 *
 * @author: 魏来
 * @date: 2022/3/2 下午3:08
 */

public class WebchatTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "webchat";
    public final AuthenticationManager authenticationManager;

    public WebchatTokenGranter(AuthorizationServerTokenServices tokenServices,
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

        // 向后台传递这个三个参数目的是：如果系统中不存在 wx用户，则根据此三个参数解析数据，并存储此wx用户
        String iv = parameters.get("iv");//加密算法的初始向量
        String code = parameters.get("code");//用户登录凭证
        String encryptedData = parameters.get("encryptedData");//加密的用户数据

        // 过河拆桥，移除后续无用参数(不移除也啥)
        parameters.remove("iv");
        parameters.remove("code");
        parameters.remove("encryptedData");


        Authentication userAuth = new WechatAuthenticationToken(code, encryptedData, iv); // 未认证状态
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        userAuth = this.authenticationManager.authenticate(userAuth); // 认证中: 跳转到WechatAuthenticationProvider.authenticate 方法中


        if (userAuth.isAuthenticated()) { // 认证成功
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else { // 认证失败
            throw new InvalidGrantException("Could not authenticate code: " + code);
        }
    }
}
