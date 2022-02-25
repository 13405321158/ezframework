/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午7:45
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: sms方式登录； 用户名+密码方式走SysUserDetailsService，wx方式走WechatAuthenticationProvider
 */
package com.leesky.ezframework.auth.details.userdetails.saler;

import com.leesky.ezframework.auth.details.userdetails.sys.SysUserDetails;
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
public class SalerDetailsService implements UserDetailsService {

    private final IbackendServerClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {

        UserDetails userDetails = null;
        Result<UserBaseDTO> ret = this.client.loadSaler(mobile, LoginTypeEnum.sms.getKey());

        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new SysUserDetails(data);
                CommonEx.throwException(userDetails);
            }
            return userDetails;
        }


        //2、然后查询卖家用户
        ret = this.client.loadSaler(mobile, LoginTypeEnum.sms.getKey());
        if (ret.isSuccess()) {
            //TODO
            CommonEx.throwException(userDetails);
            return userDetails;
        }

        //3、最后查询买家用户
        ret = this.client.loadBuyer(mobile, LoginTypeEnum.sms.getKey());
        if (ret.isSuccess()) {
            //TODO
            CommonEx.throwException(userDetails);
            return userDetails;
        }


        throw new UsernameNotFoundException("账户不存在：" + mobile);
    }


}
