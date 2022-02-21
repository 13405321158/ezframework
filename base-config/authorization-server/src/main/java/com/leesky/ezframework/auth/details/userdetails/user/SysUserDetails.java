/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:56:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails.user;

import com.leesky.ezframework.auth.enums.PasswordEncoderTypeEnum;
import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.enums.StatusEnum;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

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
    private Boolean byTime;//有效期至
    private String authenticationMethod;

    /**
     * 默认字段
     */
    private String portrait;
    private String username;
    private String password;
    private Boolean enabled;
    private String companyCode;
    private String companyName;

    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * 系统管理用户
     */
    public SysUserDetails(UserAuthDTO user) {
        this.setUserId(user.getId());
        this.setUsername(user.getUsername());
        this.setPortrait(user.getPortrait());
        this.setCompanyCode(user.getCompanyCode());
        this.setCompanyName(user.getCompanyName());
        this.setEnabled(StringUtils.equals(user.getStatus(), StatusEnum.ENABLE.getKey()));
        this.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + user.getPassword());
        this.setByTime(user.getByTime().getTime() > System.currentTimeMillis() ? true : false);
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
        return this.byTime;
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
