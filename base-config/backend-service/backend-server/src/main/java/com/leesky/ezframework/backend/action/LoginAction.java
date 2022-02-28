/*
 * @作者: 魏来
 * @日期: 2022/2/26 下午4:33
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  <li>登录控制器</li>
 */
package com.leesky.ezframework.backend.action;

import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.model.buyer.BuyerBaseModel;
import com.leesky.ezframework.backend.model.saler.SalerBaseModel;
import com.leesky.ezframework.backend.model.sys.UserBaseModel;
import com.leesky.ezframework.backend.service.IoauthCientService;
import com.leesky.ezframework.backend.service.buy.IbuyerBaseService;
import com.leesky.ezframework.backend.service.saler.IsalerBaseService;
import com.leesky.ezframework.backend.service.sys.IuserBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.I18nUtil;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import static com.leesky.ezframework.json.Result.failed;
import static com.leesky.ezframework.json.Result.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginAction {
    private final I18nUtil i18n;
    private final IuserBaseService sysService;
    private final IbuyerBaseService buyService;
    private final IsalerBaseService saleService;

    private final IoauthCientService clientService;
    /**
     * <li>平台用户登录</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/sys/{var}/{type}")
    public Result<UserBaseDTO> getSystem(@PathVariable String var, @PathVariable String type) {

        String loginType = LoginTypeEnum.getValue(type);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>(ImmutableMap.of(loginType, var));

        filter.select("id,username,status,by_time,password,ext01Id");//如果不包括ext01Id 则无法查询 ext01

        ImmutableMap<String, String> map = ImmutableMap.of("roles", "code", "ext01", "idName,company_name,portrait");
        UserBaseModel user = this.sysService.findOne(filter, map);

        if (ObjectUtils.isEmpty(user))
            return failed(this.i18n.getMsg("username.not.registered", var));

        UserBaseDTO dto = Po2DtoUtil.convertor(user, UserBaseDTO.class);

        return success(dto, false);
    }


    /**
     * <li>买家(商城会员)登录</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/buy/{var}/{type}")
    public Result<UserBaseDTO> getBuyer(@PathVariable String var, @PathVariable String type) {

        String loginType = LoginTypeEnum.getValue(type);

        QueryFilter<BuyerBaseModel> filter = new QueryFilter<>(ImmutableMap.of(loginType, var));

        filter.select("id,username,status,by_time,password,ext01Id");

        ImmutableMap<String, String> map = ImmutableMap.of("roles", "code", "ext01", "idName,company_code,company_name,portrait");
        BuyerBaseModel user = this.buyService.findOne(filter, map);


        if (ObjectUtils.isEmpty(user))
            return failed(this.i18n.getMsg("username.not.registered", var));


        UserBaseDTO dto = Po2DtoUtil.convertor(user, UserBaseDTO.class);

        return success(dto, false);
    }


    /**
     * <li>卖家登录(shop)</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/sale/{var}/{type}")
    public Result<UserBaseDTO> getSale(@PathVariable String var, @PathVariable String type) {

        String loginType = LoginTypeEnum.getValue(type);

        QueryFilter<SalerBaseModel> filter = new QueryFilter<>(ImmutableMap.of(loginType, var));

        filter.select("id,username,status,by_time,password,ext01Id");

        ImmutableMap<String, String> map = ImmutableMap.of("roles", "code", "ext01", "idName,company_code,company_name,portrait");
        SalerBaseModel user = this.saleService.findOne(filter, map);

        if (ObjectUtils.isEmpty(user))
            return failed(this.i18n.getMsg("username.not.registered", var));

        UserBaseDTO dto = Po2DtoUtil.convertor(user, UserBaseDTO.class);

        return success(dto, false);
    }


    /**
     * 查询client
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:14
     */
    @GetMapping("/client")
    public Result<OauthClientDetailsDTO> getClient(@RequestParam String clientId) {

        OauthClientDetailsModel client = this.clientService.findOne(clientId);
        Assert.isTrue(client != null, clientId + "暂未注册😉");
        OauthClientDetailsDTO dto = Po2DtoUtil.convertor(client, OauthClientDetailsDTO.class);

        return Result.success(dto,false);
    }
}
