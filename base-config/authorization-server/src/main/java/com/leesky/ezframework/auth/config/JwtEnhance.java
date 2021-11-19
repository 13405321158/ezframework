/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:16
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.auth.model.UserBaseModel;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.List;
import java.util.Map;

/**
 * <li>描述:
 */
public class JwtEnhance implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserBaseModel user = (UserBaseModel) authentication.getPrincipal();

        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);

        Map<String, String> group = Maps.newHashMap();
        List<String> roleList = Lists.newArrayList();

//        for (RoleModel role : user.getRoleSet())
//            roleList.add(role.getCode().toUpperCase());

//        for (GroupModel g : user.getGroupSet())
//            group.put(g.getId(), g.getGroupName());

        Map<String, Object> info = Maps.newHashMap(accessToken.getAdditionalInformation());
//        info.put("ext", user.jwt());// 自定义的jwt扩展信息
//        info.put("role", roleList);// 附加role信息
//        info.put("group", group);

        result.setAdditionalInformation(info);

        return result;

    }
}
