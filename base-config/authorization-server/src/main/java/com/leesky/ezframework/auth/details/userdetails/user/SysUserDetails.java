/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:56:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.details.userdetails.user;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.leesky.ezframework.auth.enums.PasswordEncoderTypeEnum;
import com.leesky.ezframework.backend.dto.UserAuthDTO;

import lombok.Data;

/**
 * 类功能说明：
 * <li>平台用户
 */
@Data
public class SysUserDetails implements UserDetails {

	private static final long serialVersionUID = -8018116003741679613L;

	/**
	 * 扩展字段
	 */
	private String userId;
	private String authenticationMethod;

	/**
	 * 默认字段
	 */
	private String username;
	private String password;
	private Boolean enabled;
	private Collection<SimpleGrantedAuthority> authorities;

	/**
	 * 系统管理用户
	 */
	public SysUserDetails(UserAuthDTO user) {
		this.setUserId(user.getId());
		this.setUsername(user.getUsername());
		this.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + user.getPassword());
		this.setEnabled(user.getStatus().equals(0));
		if (CollectionUtils.isNotEmpty(user.getRoles())) {
			authorities = new ArrayList<>();
			user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
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
		return this.enabled;
	}

}