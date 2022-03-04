/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午10:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: <li>买家(商城会员)用户管理器</li>
 */
package com.leesky.ezframework.backend.action;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.BuyerBaseModel;
import com.leesky.ezframework.backend.service.IbuyerBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.I18nUtil;
import com.leesky.ezframework.utils.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leesky.ezframework.json.Result.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerAction {

    private final I18nUtil i18n;

    private final IbuyerBaseService service;




    /**
     * 新增买家
     *
     * @author： 魏来
     * @date: 2022/2/26  上午10:10
     */
    @PostMapping(value = "/c01")
    public Result<?> addBuyer(@RequestBody UserBaseDTO dto) throws Exception {
        ValidatorUtils.all(dto);

        QueryFilter<BuyerBaseModel> filter = new QueryFilter<>();
        filter.select("id").eq("username", dto.getUsername());

        BuyerBaseModel user = this.service.findOne(filter);
        Assert.isTrue(ObjectUtils.isEmpty(user), i18n.getMsg("username.registered", dto.getUsername()));

        this.service.addUser(dto);
        return success();
    }
}
