/**
 * jwt 内容增强
 *
 * @author： 魏来
 * @date： 2021/12/10 上午10:27
 */
package com.leesky.ezframework.auth.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.leesky.ezframework.auth.details.userdetails.menber.MemberUserDetails;
import com.leesky.ezframework.auth.details.userdetails.user.SysUserDetails;
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

        //系统用户登录返回信息
        if (principal instanceof SysUserDetails) {
            SysUserDetails sysUserDetails = (SysUserDetails) principal;
            extMap.put("pubKey", Common.RSA_PUBLIC2048);
            extMap.put(Common.USER_ID, sysUserDetails.getUserId());
            extMap.put(Common.ID_NAME,sysUserDetails.getIdName());
            extMap.put(Common.USER_NAME, sysUserDetails.getUsername());
            extMap.put(Common.DEALER_CODE, sysUserDetails.getCompanyCode());
            extMap.put(Common.DEALER_NAME, sysUserDetails.getCompanyName());

            if (StringUtils.isNotBlank(sysUserDetails.getAuthenticationMethod())) {
                extMap.put("authenticationMethod", sysUserDetails.getAuthenticationMethod());
            }

        }

        //商城会员登录 返回扩展信息
        if (principal instanceof MemberUserDetails) {
            MemberUserDetails memberUserDetails = (MemberUserDetails) principal;
            extMap.put(Common.USER_ID, memberUserDetails.getUserId());
            extMap.put(Common.USER_NAME, memberUserDetails.getUsername());
            if (StringUtils.isNotBlank(memberUserDetails.getAuthenticationMethod())) {
                extMap.put("authenticationMethod", memberUserDetails.getAuthenticationMethod());
            }

        }


        ImmutableMap<String, Object> additionalInformation = ImmutableMap.of(Common.LOGIN_USER_EXT_INFO, extMap);
        //如果把extMap直接set进去，则token中不含有扩展的信息，解析token无法获取user_info信息,即在资源服务器中通过工具类JwtUtils 获取不到扩展信息
        // 但是登录成功后 返回的OAuth2AccessToken中含有user_info
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);

        return accessToken;
    }
}
