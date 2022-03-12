/*
 * @作者: 魏来
 * @日期: 2022/3/7 下午6:31
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 *  1、用户点击扫码登录，程序访问：
 *    https://open.weixin.qq.com/connect/qrconnect?appid=微信颁发的appid
 *                                                &redirect_uri=开发平台中填写的访问地址
 *                                                &response_type=code
 *                                                &scope=snsapi_login(代表微信扫描获取用户信息)
 *    显示给用户一个二维码
 *
 * 2、用户使用微信扫描 上面的二维码后，在微信中弹出确认授权按钮，用户点击同意后，程序跳转到redirect_uri。(这携带code)
 *    [redirect_url 如果携带自己的参数，则需要用 urlencode方式编码]
 * 3、用户接收code，通过下面地址获取token
 *    https://api.weixin.qq.com/sns/oauth2/access_token?appid=微信颁发的APPID
 *                                                     &secret=微信颁发的SECRET
 *                                                     &code=用户接收的code
 *                                                     &grant_type=authorization_code
 *
 * 4、获取到token后，通过下面url 获取用户信息
 *    https://api.weixin.qq.com/sns/userinfo？access_token= 第三步获取的token
 *                                          &openid= 微信颁发的appid
 *                                          &lang=zh_CN
 */
package com.leesky.ezframework.auth.ext.wx_mp;

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
 * <li>微信扫码登录</li>
 * *（系统内置一个cleint，id=wxlogin，client_secret=xxxxx,所有微信扫描登录都用这个clientid）
 *
 * @author: 魏来
 * @date: 2022/3/7 下午6:31
 */
public class ScanQrTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "wx_scan";
    public final AuthenticationManager authenticationManager;


    public ScanQrTokenGranter(AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory,
                              AuthenticationManager authenticationManager) {

        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    /**
     * <li>用户使用手机微信中 扫描二维码后，在微信中点击同意授权按钮，微信会跳转到 在开放平台中配置的redirect_url</li>
     * <li>redirect_url使用urlencode 方式编码。编码前大概是  http://域名+端口?grant_type=wxscan&client_secret=xxxx&client_id=xxxx</>
     *
     * @author: 魏来
     * @date: 2022/3/7 下午6:37
     */
    @Override
    @SneakyThrows(InvalidGrantException.class)
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = Maps.newHashMap(tokenRequest.getRequestParameters());
        String code = parameters.get("code");//用户登录凭证

        Authentication userAuth = new ScanQrAuthenticationToken(code); // 未认证状态
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        userAuth = this.authenticationManager.authenticate(userAuth); // 认证中: 跳转到 ScanQrAuthenticationProvider.authenticate 方法中


        if (userAuth.isAuthenticated()) { // 认证成功
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else { // 认证失败
            throw new InvalidGrantException("Could not authenticate code: " + code);
        }
    }
}
