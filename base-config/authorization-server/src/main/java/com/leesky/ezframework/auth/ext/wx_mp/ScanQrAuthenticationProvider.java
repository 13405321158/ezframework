/*
 * @作者: 魏来
 * @日期: 2022/3/7 下午6:57
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.ext.wx_mp;

import com.leesky.ezframework.auth.details.userdetails.buyer.BuyerDetailsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/7 下午6:57
 */
@Data
@Component
@RequiredArgsConstructor
public class ScanQrAuthenticationProvider implements AuthenticationProvider {

    private final WxMpService wxMpService;
    private final BuyerDetailsService buyerDetailsService;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        ScanQrAuthenticationToken authenticationToken = (ScanQrAuthenticationToken) authentication;
        //用户在微信中点击了 同意授权按钮，微信根据在开放平台中配置的redirect_url携带code参数。下面代码就是根据此code 经过一系列操作 最终获取微信用户的各类信息，比如昵称，微信绑定的手机号码等
        String code = (String) authenticationToken.getPrincipal();

        WxOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);

        String openId = wxMpOAuth2AccessToken.getOpenId();

        try {//使用openId查询各类用户表，如果查询不到，则需要用户扫码绑定微信
            UserDetails userDetails = buyerDetailsService.loadUserByOpenId(openId);
            ScanQrAuthenticationToken result = new ScanQrAuthenticationToken(new HashSet<>(), userDetails);
            result.setDetails(authentication.getDetails());
            return result;
        } catch (Exception e) {
            throw new UsernameNotFoundException("您的微信暂未绑定账户，请使用密码方式登录，然后点击屏幕右上方：绑定微信");
        }


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ScanQrAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
