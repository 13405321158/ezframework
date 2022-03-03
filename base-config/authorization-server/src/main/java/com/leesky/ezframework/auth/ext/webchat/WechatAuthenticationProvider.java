package com.leesky.ezframework.auth.ext.webchat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.leesky.ezframework.auth.details.userdetails.buyer.BuyerDetailsService;
import com.leesky.ezframework.backend.api.LoginClient;
import com.leesky.ezframework.backend.api.UserClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.dto.UserBaseExt01DTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.enums.StatusEnum;
import com.leesky.ezframework.json.Result;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;


@Data
@Component
@RequiredArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final WxMaService wxMaService;

    private final LoginClient client;
    private final UserClient userClient;
    private final BuyerDetailsService userDetailsService;


    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        //wx小程序访问微信后台获得的code，下面代码就是根据此code 经过一系列操作 最终获取微信用户的各类信息，比如昵称，微信绑定的手机号码等
        String code = (String) authenticationToken.getPrincipal();

        WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);//异常会抛出WxErrorException

        String openid = sessionInfo.getOpenid();

        // 微信用户不存在，注册成为新会员
        Result<UserBaseDTO> data = this.client.loadBuyer(openid, LoginTypeEnum.wx.getKey());

        if (ObjectUtils.isEmpty(data.getData())) {

            String sessionKey = sessionInfo.getSessionKey();
            String encryptedData = authenticationToken.getEncryptedData();
            String iv = authenticationToken.getIv();
            // 解密 encryptedData 获取用户信息
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

            UserBaseDTO dto = new UserBaseDTO();
            UserBaseExt01DTO ext01DTO = new UserBaseExt01DTO();
            dto.setExt01(ext01DTO);

            dto.setRemake("wxUser");
            dto.setStatus(StatusEnum.ENABLE.getKey());
            dto.setNickName(userInfo.getNickName());
            dto.setUsername(userInfo.getOpenId());
            dto.setGender(userInfo.getGender());
            ext01DTO.setCurAddress(userInfo.getCountry() + userInfo.getProvince() + userInfo.getCity());
            ext01DTO.setAvatar(userInfo.getAvatarUrl());

            dto.setByTime(LocalDateTime.of(2099, 12, 30, 12, 0, 0));
            this.userClient.addWxUser(dto);
        }
        UserDetails userDetails = userDetailsService.loadUserByOpenId(openid);
        WechatAuthenticationToken result = new WechatAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
