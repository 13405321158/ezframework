/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:49:41
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.clientdetails;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.auth.enums.PasswordEncoderTypeEnum;
import com.leesky.ezframework.auth.utils.RequestUtils;
import com.leesky.ezframework.backend.api.LoginClient;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * 类功能说明：
 * <li>登录时 查询client_id </li>
 */
@Service
@RequiredArgsConstructor
public class ClientDetailService implements ClientDetailsService {
    private final RedisService cache;
    private final LoginClient client;


    @Override
//    @Cacheable(cacheNames = "auth-token", key = "'oauth-client:'+#clientId")
    public ClientDetails loadClientByClientId(String clientId) {
        String cid = RequestUtils.getOAuth2ClientId();

        Object obj = this.cache.get(Redis.CLIENT + cid);
        if (ObjectUtils.isNotEmpty(obj))
            return JSON.parseObject(JSON.toJSONString(obj), BaseClientDetails.class);


        Result<OauthClientDetailsDTO> ret = this.client.getClient(cid);

        if (!ret.isSuccess())
            throw new BadCredentialsException(ret.getMsg());


        OauthClientDetailsDTO client = ret.getData();
        BaseClientDetails clientDetails = new BaseClientDetails(
                client.getClientId(),
                client.getResourceIds(),
                client.getScope(),
                client.getAuthorizedGrantTypes(),
                client.getAuthorities(),
                client.getWebServerRedirectUri()
        );
        clientDetails.setClientSecret(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + client.getClientSecret());
        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());

        this.cache.add(Redis.CLIENT + cid, clientDetails);
        return clientDetails;


    }

}
