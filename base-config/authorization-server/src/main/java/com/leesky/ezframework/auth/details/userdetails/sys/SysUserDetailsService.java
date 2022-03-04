/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:09:51
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 用户名+密码、 用户名+密码+图片验证方式；sms登录方式走SmsCodeAuthenticationProvider，wx方式走WechatAuthenticationProvider
 */
package com.leesky.ezframework.auth.details.userdetails.sys;

import com.leesky.ezframework.auth.details.userdetails.CommonCode;
import com.leesky.ezframework.backend.api.LoginClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.json.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <li>用户名+密码、用户名+密码+图片验证码 方式默认走这个方法</li>
 * <li>grant_type = password 走这个方法</li>
 *
 * @author: 魏来
 * @date: 2022/3/4 下午5:53
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final LoginClient loginClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        //1、查询 系统用户
        Result<UserBaseDTO> ret = this.loginClient.loadSys(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess())
            return CommonCode.loadUser(ret);


        //2、查询卖家用户
        ret = this.loginClient.loadSaler(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess())
            return CommonCode.loadSaler(ret);


        //3、查询买家用户
        ret = this.loginClient.loadBuyer(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess())
            return CommonCode.loadBuyer(ret);

        //4、查询经销商
        ret = this.loginClient.loadDealer(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess())
            return CommonCode.loadUser(ret);

        throw new UsernameNotFoundException(username+":账户未注册");
    }


}
