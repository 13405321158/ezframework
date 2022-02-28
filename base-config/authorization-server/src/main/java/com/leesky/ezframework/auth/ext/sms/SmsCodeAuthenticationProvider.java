package com.leesky.ezframework.auth.ext.sms;


import com.google.common.collect.Sets;
import com.leesky.ezframework.auth.details.userdetails.saler.SalerDetailsService;
import com.leesky.ezframework.auth.utils.RequestUtils;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.redis.service.RedisService;
import com.leesky.ezframework.utils.I18nUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 短信验证码认证授权提供者
 */
@Data
@Component
@RequiredArgsConstructor
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private final I18nUtil i18nUtil;
    private final RedisService cache;
    private final SalerDetailsService userDetailsService;//在WebSecurityConfig中配置了 短信方式登录 默认是SalerDetailsService


    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        //1、接收前端传递手机号码和验证码
        String mobile = (String) authenticationToken.getPrincipal();
        String paramCode = RequestUtils.getCode();

        Assert.isTrue(StringUtils.isNotBlank(mobile), i18nUtil.getMsg("login.type.sms.mobile.null"));

        //2、从缓存中取出sms验证码 和 传递过来的 做对比
        String codeKey = Redis.SMS_KEY + mobile;
        String redisCode = (String) this.cache.get(codeKey);
        Assert.isTrue(StringUtils.isNotBlank(redisCode), paramCode + "：已过期或伪造的SMS");
        // 验证码比对
        if (redisCode.equals(paramCode)) {
            this.cache.del(codeKey); // 比对成功删除缓存的验证码

            UserDetails userDetails = this.userDetailsService.loadUserByMobile(mobile);
            SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(), Sets.newHashSet());
            result.setDetails(authentication.getDetails());
            return result;
        }

        throw new IllegalArgumentException("验证码不匹配");


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
