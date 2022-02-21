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

    private RedisService cache;
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
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();

            Map<String, String> parameters = Maps.newHashMap(tokenRequest.getRequestParameters());
            // 验证码校验逻辑
            String key = request.getHeader("key");
            String value = request.getHeader("code");


            Assert.isTrue(StringUtils.isNotBlank(value), "验证码不能为空");

            // 从缓存取出正确的验证码和用户输入的验证码比对，然后删除
            String validateCodeKey = Redis.LOGIN_IMG_CODE + key;
            String correctValidateCode = (String) this.cache.get(validateCodeKey);
            Assert.isTrue(StringUtils.equals(correctValidateCode, value), "验证码错误");
            this.cache.del(validateCodeKey);


            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // 移除后续无用参数
            parameters.remove("password");


            // 和密码模式一样的逻辑
            Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
            ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

            try {
                userAuth = this.authenticationManager.authenticate(userAuth);
            } catch (AccountStatusException e0) {
                throw new InvalidGrantException(e0.getMessage());
            } catch (BadCredentialsException e1) {
                throw new InvalidGrantException(e1.getMessage());
            }


            if (userAuth != null && userAuth.isAuthenticated()) {
                OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, userAuth);
            } else {
                throw new InvalidGrantException("Could not authenticate user: " + username);
            }
        }


    }

}
