/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午10:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: <li>买家(商户控制器)</li>
 */
package com.leesky.ezframework.backend.action;

import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.backend.model.saler.SalerBaseModel;
import com.leesky.ezframework.backend.service.saler.IsalerBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.I18nUtil;
import com.leesky.ezframework.utils.Po2DtoUtil;
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
@RequestMapping("/sale-user")
public class SalerAction {

    private final I18nUtil i18n;

    private final IsalerBaseService service;

    /**
     * <li>登录获取token时使用</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{var}/type/public")
    public Result<UserBaseDTO> loadUserByUsername(@PathVariable String var, @PathVariable String type) {

        String loginType = LoginTypeEnum.getValue(type);

        QueryFilter<SalerBaseModel> filter = new QueryFilter<>(ImmutableMap.of(loginType, var));

        filter.select("id,username,status,by_time,password,ext01Id");

        ImmutableMap<String, String> map = ImmutableMap.of("roles", "code", "ext01", "idName,company_code,company_name,portrait");
        SalerBaseModel user = this.service.findOne(filter, map);


        Assert.isTrue(ObjectUtils.isNotEmpty(user), this.i18n.getMsg("username.not.registered", var));


        UserBaseDTO dto = Po2DtoUtil.convertor(user, UserBaseDTO.class);

        return success(dto, false);
    }
}
