/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:54:48
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails.buyer;

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
 * <li>微信一键登录走这个方法</li>
 * * <li>grant_type = webchat 走这个方法</li>
 *
 * @author: 魏来
 * @date: 2022/3/4 下午6:14
 */
@Service
@RequiredArgsConstructor
public class BuyerDetailsService implements UserDetailsService {

    private final LoginClient loginClient;


    public UserDetails loadUserByOpenId(String openId) throws UsernameNotFoundException {


        //1、系统用户
        Result<UserBaseDTO> ret = this.loginClient.loadSys(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess())
            return CommonCode.loadUser(ret);

        //2、买家
        ret = this.loginClient.loadBuyer(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess())
            return CommonCode.loadSaler(ret);

        //3、卖家
        ret = this.loginClient.loadSaler(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess())
            return CommonCode.loadBuyer(ret);

        //4、经销商
        ret = this.loginClient.loadDealer(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess())
            return CommonCode.loadUser(ret);

        throw new UsernameNotFoundException(openId+":账户未注册");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
