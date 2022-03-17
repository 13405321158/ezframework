package com.leesky.ezframework.auth.ext.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 手机验证码登录
 * 注册当天 client_secret = md5(mobile  + 年-月-日)； 每天零点 定时任务更新 client=手机号码的 的 记录.
 * 所以前端登录时 Authorization 参数= Basic + md5(mobile + 年-月-日)
 *
 * @author: 魏来
 * @date: 2022/3/12 上午10:23
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 7869145909948215123L;

    private Object credentials;
    private final Object principal;

    //这里代表未认证，此方法用在SmsCodeTokenGranter中被构造
    public SmsCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    //这里代表已认证，此方法用在SmsCodeAuthenticationProvider中被构造
    public SmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Authentication authentication) {
        super(authorities);
        this.principal = principal;
        this.setDetails(authentication.getDetails());
        this.credentials = authentication.getCredentials();
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
