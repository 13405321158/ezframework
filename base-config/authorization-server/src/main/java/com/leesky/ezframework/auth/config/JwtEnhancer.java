/**
 * jwt 内容增强
 *
 * @author： 魏来
 * @date： 2021/12/10 上午10:27
 */
package com.leesky.ezframework.auth.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.leesky.ezframework.auth.details.userdetails.buyer.BuyerDetails;
import com.leesky.ezframework.auth.details.userdetails.saler.SalerDetails;
import com.leesky.ezframework.auth.details.userdetails.sys.SysUserDetails;
import com.leesky.ezframework.global.Common;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class JwtEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> extMap = Maps.newHashMap();
        Object principal = authentication.getPrincipal();

        extMap.put("pubKey", Common.RSA_PUBLIC2048);
        //系统用户登录返回信息
        if (principal instanceof SysUserDetails) {
            SysUserDetails details = (SysUserDetails) principal;

            extMap.put(Common.USER_ID, details.getUserId());
            extMap.put(Common.ID_NAME, details.getIdName());
            extMap.put(Common.USER_NAME, details.getUsername());
            extMap.put(Common.COMPANY_NAME, details.getCompanyName());

            if (StringUtils.isNotBlank(details.getAuthenticationMethod()))
                extMap.put("authenticationMethod", details.getAuthenticationMethod());


        }

        //商城会员登录 返回买家扩展信息
        if (principal instanceof BuyerDetails) {
            BuyerDetails details = (BuyerDetails) principal;

            extMap.put(Common.USER_ID, details.getUserId());
            extMap.put(Common.ID_NAME, details.getIdName());
            extMap.put(Common.USER_NAME, details.getUsername());
            extMap.put(Common.COMPANY_NAME, details.getCompanyName());

            if (StringUtils.isNotBlank(details.getAuthenticationMethod()))
                extMap.put("authenticationMethod", details.getAuthenticationMethod());

        }
        //返回卖家扩展信息
        if (principal instanceof SalerDetails) {
            SalerDetails details = (SalerDetails) principal;
            extMap.put(Common.USER_ID, details.getUserId());
            extMap.put(Common.ID_NAME, details.getIdName());
            extMap.put(Common.USER_NAME, details.getUsername());
            extMap.put(Common.DEALER_CODE, details.getDealerCode());
            extMap.put(Common.DEALER_NAME, details.getDealerName());
            if (StringUtils.isNotBlank(details.getAuthenticationMethod()))
                extMap.put("authenticationMethod", details.getAuthenticationMethod());

        }


        ImmutableMap<String, Object> additionalInformation = ImmutableMap.of(Common.LOGIN_USER_EXT_INFO, extMap);
        //如果把extMap直接set进去，则token中不含有扩展的信息，解析token无法获取user_info信息,即在资源服务器中通过工具类JwtUtils 获取不到扩展信息
        // 但是登录成功后 返回的OAuth2AccessToken中含有user_info
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);

        return accessToken;
    }
}
