/*
 * @作者: 魏来
 * @日期: 2022/3/7 下午6:54
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.ext.wx_mp;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/7 下午6:54
 */
public class ScanQrAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1000505007877883720L;
    private Object credentials;
    private final Object principal;

    public ScanQrAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
    }

    public ScanQrAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }
    public ScanQrAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
