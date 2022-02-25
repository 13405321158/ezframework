package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.enums.LoginTypeEnum;
import com.leesky.ezframework.backend.model.sys.UserBaseModel;
import com.leesky.ezframework.backend.service.sys.IuserBaseService;
import com.leesky.ezframework.backend.vo.UserBaseVO;
import com.leesky.ezframework.es.annotation.SysLogger;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.CommonDTO;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.I18nUtil;
import com.leesky.ezframework.utils.Po2DtoUtil;
import com.leesky.ezframework.utils.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leesky.ezframework.json.Result.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-user")
public class SysUserAction {

    private final I18nUtil i18n;

    private final IuserBaseService service;

    /**
     * <li>登录获取token时使用</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{var}/{type}/public")
    @SysLogger(module = "系统用户控制器", action = "系统用户登录")
    public Result<UserBaseDTO> loadUser(@PathVariable String var, @PathVariable String type) {

        String loginType = LoginTypeEnum.getValue(type);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>(ImmutableMap.of(loginType, var));

        filter.select("id,username,status,by_time,password,ext01Id");//如果不包括ext01Id 则无法查询 ext01

        ImmutableMap<String, String> map = ImmutableMap.of("roles", "code", "ext01", "idName,company_code,company_name,portrait");
        UserBaseModel user = this.service.findOne(filter, map);

        Assert.isTrue(ObjectUtils.isNotEmpty(user), this.i18n.getMsg("username.not.registered", var));

        UserBaseDTO dto = Po2DtoUtil.convertor(user, UserBaseDTO.class);

        return success(dto, false);
    }


    /**
     * 系统用户列表
     *
     * @author： 魏来
     * @date: 2022/1/7 下午3:31
     */
    @PostMapping(value = "/r01")
    @SysLogger(module = "系统用户控制器", action = "系统用户列表")
    public Result<List<UserBaseVO>> r01(@RequestBody ParamModel param) {
        QueryFilter<UserBaseModel> filter = new QueryFilter<>(param);
//        filter.select("id,username,status,ext01.idCard");
        Page<UserBaseVO> data = this.service.page(filter, UserBaseVO.class);

        return success(data.getRecords(), data.getTotal(), false);
    }

    /**
     * <li>新增用户，同时新增client</li>
     *
     * @author： 魏来
     * @date: 2021年12月3日 上午9:26:01
     */
    @PostMapping("/c01")
    public Result<UserBaseDTO> add(@RequestBody UserBaseDTO dto) throws Exception {
        ValidatorUtils.valid(dto);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.select("id").eq("username", dto.getUsername());

        UserBaseModel user = this.service.findOne(filter);
        Assert.isTrue(ObjectUtils.isEmpty(user), i18n.getMsg("username.registered", dto.getUsername()));

        this.service.addUser(dto);

        return success();
    }

    /**
     * 编辑用户信息
     *
     * @author： 魏来
     * @date: 2022/1/29  16:06
     */
    @PostMapping(value = "/c02")
    public Result<?> edit(@RequestBody UserBaseDTO dto) throws Exception {
        ValidatorUtils.valid(dto);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.eq("id", dto.getId());
        UserBaseModel origin = this.service.findOne(filter, ImmutableMap.of("ext01", "*", "ext02", "*"));
        Assert.isTrue(ObjectUtils.isNotEmpty(origin), this.i18n.getMsg("username.not.registered", dto.getUsername()));

        UserBaseModel dest = Po2DtoUtil.convertor(dto, UserBaseModel.class);
        BeanUtils.copyProperties(origin, dest);

        this.service.editUser(dest);

        return success();
    }

    /**
     * 用户修改密码
     *
     * @author： 魏来
     * @date: 2022/2/9  下午5:48
     */
    @PostMapping(value = "/c03")
    public Result<?> editPwd(@RequestBody CommonDTO dto) {
        Object pwd = dto.getObj().get("pwd");
        Object userId = dto.getObj().get("uid");
        Object client = dto.getObj().get("client");

        Assert.isTrue(ObjectUtils.isNotEmpty(pwd) && ObjectUtils.isNotEmpty(userId) && ObjectUtils.isNotEmpty(client), "修改密码时参数：用户Id、用户名和新密码不允许空");
        this.service.editPwd((String) userId, (String) client, (String) pwd);

        return success();
    }

    /**
     * 删除用户
     *
     * @author： 魏来
     * @date: 2022/1/25  17:05
     */
    @PostMapping(value = "/d01")
    public Result<?> d01(@RequestBody List<UserBaseDTO> list) {

        list.forEach(e -> {
            Assert.isTrue(StringUtils.isNotBlank(e.getId()), "参数userId不允许空值");
            Assert.isTrue(StringUtils.isNotBlank(e.getUsername()), "参数loginName不允许空值");
        });


        this.service.delUser(list);

        return success();
    }

}
