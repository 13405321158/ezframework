/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午7:45
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: sms方式登录； 用户名+密码方式走SysUserDetailsService，wx方式走WechatAuthenticationProvider
 */
package com.leesky.ezframework.auth.details.userdetails.saler;

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


@Service
@RequiredArgsConstructor
public class SalerDetailsService implements UserDetailsService {

    private final LoginClient loginClient;


    /**
     * <li>sms登录方式 默认进入这个方法，这是在WebSecurityConfig中配置了</li>
     *
     * @author: 魏来
     * @date: 2022/2/26 上午11:04
     */
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {


        //1、然后查询系统用户
        Result<UserBaseDTO> ret = this.loginClient.loadSys(mobile, LoginTypeEnum.sms.getKey());

        if (ret.isSuccess())
            return CommonCode.loadUser(ret);

        //2、卖家
        ret = this.loginClient.loadSaler(mobile, LoginTypeEnum.sms.getKey());
        if (ret.isSuccess())
            return CommonCode.loadSaler(ret);


        //3、买家
        ret = this.loginClient.loadBuyer(mobile, LoginTypeEnum.sms.getKey());
        if (ret.isSuccess())
            return CommonCode.loadBuyer(ret);

        //4、查询经销商
        ret = this.loginClient.loadDealer(mobile, LoginTypeEnum.password.getKey());
        if (ret.isSuccess())
            return CommonCode.loadUser(ret);

        throw new UsernameNotFoundException(mobile + ":账户未注册");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

}
