/*
 * @作者: 魏来
 * @日期: 2021年11月29日  下午2:53:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.action;


import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.redis.service.RedisService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class TokenAction {

    private final KeyPair keyPair;
    private final RedisService cache;
    private final TokenEndpoint tokenEndpoint;

    @Value("${access.token.validity:600}") // 默认值10分钟
    private String accessTokenValiditySeconds;

    /**
     * 描述
     *
     * @author： 魏来
     * @date: 2022/1/21  10:15
     */
    @PostMapping("/token")
    public Result<Map<String, String>> getToken(Principal principal, @RequestParam Map<String, String> map) throws HttpRequestMethodNotSupportedException {

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, map).getBody();
        Map<String, String> userInfo = add2Cache(accessToken);

        return Result.success(userInfo, false);
    }


    /**
     * 用途:资源服务器调用，验证token的合法性，因为授权服务器发放的jwt内容使用rsa非对称算法加密了
     *
     * @author： 魏来
     * @date: 2021/12/7 上午10:54
     */
    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }


    /**
     * 登录用户id、登录用户username、登录用户token缓存到redis中
     *
     * @author： 魏来
     * @date: 2021/12/14 下午12:25
     */

    private Map<String, String> add2Cache(OAuth2AccessToken accessToken) {
        Long expr = Long.valueOf(accessTokenValiditySeconds);//1、有效期时长
        var ext = (Map<String, String>) accessToken.getAdditionalInformation().get(Common.LOGIN_USER_EXT_INFO);

        this.cache.add(Redis.AUTH_TOKEN_ID + ext.get(Common.USER_ID), accessToken.getValue(), expr);//2、登录用户id和token之间关系

        return ext;
    }


//    @GetMapping("/weilai")
//    public void bindWechat(HttpServletResponse response, HttpServletRequest request) throws Exception {
//        String id = request.getParameter("id");
//        String callback = "http://leesky.natapp1.cc/author/oauth/weilai01?userid="+id;
//        // 获取code的url
//        String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxb57d46e4f60cd731&redirect_uri=" +
//                //回调地址，即用户扫码授权后跳转到的页面  会自动带上code、state参数
//                URLEncoder.encode(callback, "UTF-8") +
//                "&response_type=code" +
//                "&scope=snsapi_login";
//
//        response.sendRedirect(url);
//    }
//
//    @GetMapping("/weilai01")
//    public String getid(HttpServletRequest request) throws Exception {
//        String id = request.getParameter("userid");
//        System.out.println("id=" + id);
//        return "账户绑定微信成功：您可以使用微信扫码登录了";
//    }
}
