/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午10:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: <li>买家(商城会员)用户管理器</li>
 */
package com.leesky.ezframework.backend.action;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.utils.I18nUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leesky.ezframework.json.Result.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer-user")
public class BuyerAction {

    private final I18nUtil i18n;

    /**
     * <li>登录获取token时使用</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{username}/public")
    public Result<UserBaseDTO> loadUserByUsername(@PathVariable String username) {


        Object user = null;

        Assert.isTrue(ObjectUtils.isNotEmpty(user), this.i18n.getMsg("username.not.registered", username));


        return success();
    }

}
