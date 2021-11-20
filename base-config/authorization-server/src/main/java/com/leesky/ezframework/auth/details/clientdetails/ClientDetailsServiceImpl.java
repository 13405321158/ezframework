/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:49:41
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.details.clientdetails;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * 类功能说明：
 * <li></li>
 */
public class ClientDetailsServiceImpl implements ClientDetailsService{

	@Override
    @Cacheable(cacheNames = "auth", key = "'oauth-client:'+#clientId")
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		return null;
	}

}
