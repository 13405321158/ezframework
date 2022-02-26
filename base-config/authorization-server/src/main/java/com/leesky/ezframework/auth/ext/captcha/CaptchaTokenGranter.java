/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:33:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @see org.springframework.security.oauth2.provider.CompositeTokenGranter#grant(String, TokenRequest)
 * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#grant(String, TokenRequest)
 */
package com.leesky.ezframework.auth.ext.captcha;

import com.google.common.collect.Maps;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 类功能说明：
 * <li>动态图形验证码授权
 * <li>声明授权者 CaptchaTokenGranter 支持授权模式 captcha
 * <li>根据接口传值 grant_type = captcha的值匹配到此授权者
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "captcha";

    private final RedisService cache;
    public AuthenticationManager authenticationManager;

    public CaptchaTokenGranter(AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory,
                               AuthenticationManager authenticationManager,
                               RedisService redisTemplate
    ) {

        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);

        this.cache = redisTemplate;
        this.authenticationManager = authenticationManager;

    }


    @Override
    @SneakyThrows(InvalidGrantException.class)
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        {
            // 1、从请求头部获取 验证码
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert servletRequestAttributes != null;
            HttpServletRequest request = servletRequestAttributes.getRequest();

            String key = request.getHeader("key");
            String value = request.getHeader("code");
            Assert.isTrue(StringUtils.isNotBlank(value), "请输入验证码");

            // 从缓存取出正确的验证码和用户输入的验证码比对，然后删除
            String validateCodeKey = Redis.LOGIN_IMG_CODE + key;
            String correctValidateCode = (String) this.cache.get(validateCodeKey);
            Assert.isTrue(StringUtils.equals(correctValidateCode, value), "验证码不匹配");
            //this.cache.del(validateCodeKey);

            // 和密码模式一样的逻辑
            Map<String, String> parameters = Maps.newHashMap(tokenRequest.getRequestParameters());
            Authentication userAuth = new UsernamePasswordAuthenticationToken(parameters.get("username"), parameters.get("password"));
            ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

            OAuth2Request storedOAuth2Request = null;

            userAuth = this.authenticationManager.authenticate(userAuth);//系统默认执行SysUserDetailsService


            if (userAuth.isAuthenticated())
                storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);


            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        }


    }

}
