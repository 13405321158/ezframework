/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午5:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 抽取的通用代码
 */
package com.leesky.ezframework.auth.exception;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;


public class CommonEx {

    public static void throwException(UserDetails userDetails) {
        if (!userDetails.isEnabled())
            throw new DisabledException("该账户已被禁用!");

        if (!userDetails.isAccountNonLocked())
            throw new LockedException("该账号已被锁定!");


        if (!userDetails.isAccountNonExpired())
            throw new AccountExpiredException("该账号已过期!");

    }
}
