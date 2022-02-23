/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:51:31
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.details.userdetails.buyer;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 类功能说明：
 * <li>商城会员
 */
@Data
public class BuyerDetails implements UserDetails {

	private static final long serialVersionUID = 2757217808840024326L;

	private String userId;
	private String username;
	private Boolean enabled;

	/**
	 * 认证方式
	 */
	private String authenticationMethod;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Sets.newHashSet();
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
