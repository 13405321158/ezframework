/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:19
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.enums.StatusEnum;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * <li>描述:
 */
@Getter
@Setter
@TableName("cbm_mag_user")
public class UserBaseModel extends BaseUuidModel implements UserDetails {

    private String username;
    private String password;
    private String status;

    public UserBaseModel() {
        this.status = StatusEnum.ENABLE.getKey();// 账户状态是否被锁定 0 =正常，1=停止使用
    }

    public UserBaseModel(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = StatusEnum.ENABLE.getKey();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
    public boolean isAccountNonExpired() {    // 账户是否过期,过期无法验证
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {// 指定用户是否被锁定或者解锁,锁定的用户无法进行身份验证
        return this.status.equals(StatusEnum.ENABLE.getKey()) ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
        return false;
    }

    @Override
    public boolean isEnabled() { // 是否被禁用,禁用的用户不能身份验证
        return false;
    }
}
