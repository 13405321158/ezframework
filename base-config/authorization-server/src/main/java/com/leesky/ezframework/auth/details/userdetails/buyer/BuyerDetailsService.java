/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午3:54:48
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails.buyer;

import com.leesky.ezframework.auth.details.userdetails.saler.SalerDetails;
import com.leesky.ezframework.auth.details.userdetails.sys.SysUserDetails;
import com.leesky.ezframework.auth.exception.CommonEx;
import com.leesky.ezframework.backend.api.LoginClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.json.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 类功能说明：
 * <li>商城会员: wx登录方式默认进入这个方法
 */
@Service
@RequiredArgsConstructor
public class BuyerDetailsService implements UserDetailsService {

    private final LoginClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    public UserDetails loadUserByOpenId(String openId) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        //1、首先查询 买家(商城会员)用户
        Result<UserBaseDTO> ret = this.client.loadBuyer(openId, LoginTypeEnum.wx.getKey());

        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new BuyerDetails(data);
                CommonEx.throwException(userDetails);
            }
            return userDetails;
        }
        //2、然后查询卖家(商户)用户
        ret = this.client.loadDealer(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new SalerDetails(data);
                CommonEx.throwException(userDetails);
            }
            return userDetails;
        }
        //3、然后查询系统用户
        ret = this.client.loadSys(openId, LoginTypeEnum.wx.getKey());
        if (ret.isSuccess()) {
            UserBaseDTO data = ret.getData();
            if (ObjectUtils.isNotEmpty(data)) {
                userDetails = new SysUserDetails(data);
                CommonEx.throwException(userDetails);
            }
            return userDetails;
        }

        throw new UsernameNotFoundException("账户不存在：" + openId);
    }
}
