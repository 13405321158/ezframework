/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:09:51
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails.sys;

import com.leesky.ezframework.backend.api.IbackendServerClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.json.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 类功能说明：
 * <li>系统管理(平台)用户
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final IbackendServerClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        //1、首先查询 系统用户
        Result<UserBaseDTO> ret = this.client.loadSystemUserByUsername(username);
        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new SysUserDetails(data);
                throwException(userDetails);
            }
            return userDetails;
        }

        //2、然后查询卖家用户
        ret = this.client.loadSalerUserByUsername(username);
        if (ret.isSuccess()) {
             //TODO
            throwException(userDetails);
            return userDetails;
        }

        //3、最后查询买家用户
        ret = this.client.loadBuyerUserByUsername(username);
        if (ret.isSuccess()) {
            //TODO
            throwException(userDetails);
            return userDetails;
        }


        throw new UsernameNotFoundException("账户不存在：" + username);
    }

    private void throwException(UserDetails userDetails) {
        if (!userDetails.isEnabled())
            throw new DisabledException("该账户已被禁用!");

        if (!userDetails.isAccountNonLocked())
            throw new LockedException("该账号已被锁定!");


        if (!userDetails.isAccountNonExpired())
            throw new AccountExpiredException("该账号已过期!");

    }
}
