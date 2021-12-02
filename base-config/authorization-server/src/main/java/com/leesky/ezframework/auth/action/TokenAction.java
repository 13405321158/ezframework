/*
 * @作者: 魏来
 * @日期: 2021年11月29日  下午2:53:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.action;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.json.AjaxJson;

import lombok.AllArgsConstructor;

/**
 * 类功能说明：
 * <li></li>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class TokenAction {

	private final TokenEndpoint tokenEndpoint;

	@PostMapping("/token")
	public AjaxJson<OAuth2AccessToken> getToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

		AjaxJson<OAuth2AccessToken> json = new AjaxJson<>();

		OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
		json.setData(accessToken);

		return json;

	}
}
