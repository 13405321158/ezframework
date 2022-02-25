/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:09:51
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 用户名+密码、 用户名+密码+图片验证方式；sms登录方式走SmsCodeAuthenticationProvider，wx方式走WechatAuthenticationProvider
 */
package com.leesky.ezframework.auth.details.userdetails.sys;

import com.leesky.ezframework.auth.exception.CommonEx;
import com.leesky.ezframework.backend.api.IbackendServerClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.json.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final IbackendServerClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        //1、首先查询 系统用户
        Result<UserBaseDTO> ret = this.client.loadSystemUser(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new SysUserDetails(data);
                CommonEx.throwException(userDetails);
            }
            return userDetails;
        }

        //2、然后查询卖家用户
        ret = this.client.loadSaler(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess()) {
            //TODO
            CommonEx.throwException(userDetails);
            return userDetails;
        }

        //3、最后查询买家用户
        ret = this.client.loadBuyer(username, LoginTypeEnum.password.getKey());
        if (ret.isSuccess()) {
            //TODO
            CommonEx.throwException(userDetails);
            return userDetails;
        }


        throw new UsernameNotFoundException("账户不存在：" + username);
    }


}
