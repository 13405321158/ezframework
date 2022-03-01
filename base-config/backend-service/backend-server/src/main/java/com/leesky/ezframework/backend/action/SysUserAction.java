package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.leesky.ezframework.json.Result.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-user")
public class SysUserAction {

    private final I18nUtil i18n;

    private final IuserBaseService service;


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
    @PostMapping("/c01/public")
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
     * 用户修改密码: 前端html对pwd明文使用md5(32位小写)加密，所以这里接到的是 md5字符串
     *
     * @author： 魏来
     * @date: 2022/2/9  下午5:48
     */
    @PostMapping(value = "/c03")
    public Result<?> editPwd(@RequestBody CommonDTO dto) {
        Object pwd = dto.getObj().get("pwd");
        Object userId = dto.getObj().get("uid");


        Assert.isTrue(ObjectUtils.isNotEmpty(pwd) && ObjectUtils.isNotEmpty(userId), this.i18n.getMsg("user.edit.param.null"));
        this.service.editPwd((String) userId, (String) pwd);

        return success();
    }

    /**
     * 账户禁用（支持批量操作）
     *
     * @author： 魏来
     * @date: 2022/3/1  上午8:00
     */
    @PostMapping(value = "/c01")
    public Result<?> disable(@RequestBody CommonDTO dto) {

        List<String> uids = dto.getCid();
        Assert.isTrue(CollectionUtils.isNotEmpty(uids), this.i18n.getMsg("user.status.disable.null"));


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
