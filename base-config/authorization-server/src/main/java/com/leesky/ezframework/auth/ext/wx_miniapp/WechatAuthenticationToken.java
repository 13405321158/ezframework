package com.leesky.ezframework.auth.ext.wx_miniapp;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 声明授权者 CaptchaTokenGranter 支持授权模式 wechat
 * 根据接口传值 grant_type = wechat 的值匹配到此授权者
 * 匹配逻辑详见下面的两个方法
 *
 * @see org.springframework.security.oauth2.provider.CompositeTokenGranter#grant(String, TokenRequest)
 * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#grant(String, TokenRequest)
 */

public class WechatAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 7331980402741476816L;

    @Getter
    private String iv;
    @Getter
    private String encryptedData;
    private final Object principal;

    /**
     * 账号校验之前的token构建
     *
     * @param principal
     */
    public WechatAuthenticationToken(Object principal, String encryptedData, String iv) {
        super(null);
        this.principal = principal;
        this.encryptedData = encryptedData;
        this.iv = iv;
        setAuthenticated(false);
    }

    /**
     * 账号校验成功之后的token构建
     *
     * @param principal
     * @param authorities
     */
    public WechatAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(isAuthenticated == false, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
