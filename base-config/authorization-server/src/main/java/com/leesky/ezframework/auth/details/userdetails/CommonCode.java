/*
 * @作者: 魏来
 * @日期: 2022/3/4 下午6:21
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails;

import com.leesky.ezframework.auth.details.userdetails.buyer.BuyerDetails;
import com.leesky.ezframework.auth.details.userdetails.saler.SalerDetails;
import com.leesky.ezframework.auth.details.userdetails.sys.SysUserDetails;
import com.leesky.ezframework.auth.exception.CommonEx;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.json.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/4 下午6:21
 */
public class CommonCode {
    static UserDetails userDetails = null;
    public static UserDetails loadUser(Result<UserBaseDTO> ret) {

        UserBaseDTO data = ret.getData();
        if (ObjectUtils.isNotEmpty(data)) {
            userDetails = new SysUserDetails(data);
            CommonEx.throwException(userDetails);
        }
        return userDetails;

    }

    public static UserDetails loadSaler(Result<UserBaseDTO> ret) {

        UserBaseDTO data = ret.getData();
        if (ObjectUtils.isNotEmpty(data)) {
            userDetails = new SalerDetails(data);
            CommonEx.throwException(userDetails);
        }
        return userDetails;

    }


    public static UserDetails loadBuyer(Result<UserBaseDTO> ret) {

        UserBaseDTO data = ret.getData();
        if (ObjectUtils.isNotEmpty(data)) {
            userDetails = new BuyerDetails(data);
            CommonEx.throwException(userDetails);
        }
        return userDetails;

    }
}
