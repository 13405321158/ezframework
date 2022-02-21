package com.leesky.ezframework.auth.ext.webchat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.leesky.ezframework.auth.details.userdetails.menber.MemberUserDetailsServiceImpl;
import com.leesky.ezframework.backend.api.IbackendServerClient;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;

/**
 * 微信认证提供者
 *
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2021/9/25
 */
@Data
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private WxMaService wxMaService;
    private UserDetailsService userDetailsService;
    private IbackendServerClient memberFeignClient;

    /**
     * 微信认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();

        WxMaJscode2SessionResult sessionInfo ;

        sessionInfo = wxMaService.getUserService().getSessionInfo(code);

        String openid = sessionInfo.getOpenid();
//        memberAuthResult = memberFeignClient.loadUserByOpenId(openid);
//        // 微信用户不存在，注册成为新会员
//        if (memberAuthResult != null && ResultCode.USER_NOT_EXIST.getCode().equals(memberAuthResult.getCode())) {
//
//            String sessionKey = sessionInfo.getSessionKey();
//            String encryptedData = authenticationToken.getEncryptedData();
//            String iv = authenticationToken.getIv();
//            // 解密 encryptedData 获取用户信息
//            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
//
//            UmsMember member = new UmsMember();
//            BeanUtil.copyProperties(userInfo, member);
//            member.setOpenid(openid);
//            member.setStatus(GlobalConstants.STATUS_YES);
//            memberFeignClient.add(member);
//        }
        UserDetails userDetails = ((MemberUserDetailsServiceImpl) userDetailsService).loadUserByOpenId(openid);
        WechatAuthenticationToken result = new WechatAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
