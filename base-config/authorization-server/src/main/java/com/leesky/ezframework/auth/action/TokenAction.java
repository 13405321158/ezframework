/*
 * @作者: 魏来
 * @日期: 2021年11月29日  下午2:53:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.action;

import com.leesky.ezframework.json.AjaxJson;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 类功能说明：
 * <li></li>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class TokenAction {

    private final KeyPair keyPair;

    private final TokenEndpoint tokenEndpoint;

    @PostMapping("/token")
    public AjaxJson<OAuth2AccessToken> getToken(Principal principal, @RequestParam Map<String, String> map) throws HttpRequestMethodNotSupportedException {
        Assert.isTrue(StringUtils.isNotBlank(map.get("password")), "参数password不允许空值");
        Assert.isTrue(StringUtils.isNotBlank(map.get("grant_type")), "参数grant_type不允许空值");
        Assert.isTrue(StringUtils.isNotBlank(map.get("client_secret")), "参数client_secret不允许空值");

        AjaxJson<OAuth2AccessToken> json = new AjaxJson<>();

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, map).getBody();
        json.setData(accessToken);


        return json;

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
}
