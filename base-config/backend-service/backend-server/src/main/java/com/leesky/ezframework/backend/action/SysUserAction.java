package com.leesky.ezframework.backend.action;

import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IroleService;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * desc TODO
 *
 * @author 魏来
 * @date 2021/12/1 下午6:39
 */

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserAction {

    private final IuserBaseService service;

    private final IroleService roleService;

    private final JwtUtils JwtUtils;

    /**
     * <li>登录获取token时使用</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{username}/public")
    public Result<UserAuthDTO> getUserByUsername(@PathVariable String username) {

        ParamModel param = new ParamModel(ImmutableMap.of("Q_username_EQ", username));

        param.setSelect("id,username,status,by_time,password");

        QueryFilter<UserBaseModel> filter = new QueryFilter<>(param, UserBaseModel.class);

        UserBaseModel user = this.service.findOne(filter, ImmutableMap.of("roles", "code"));

        UserAuthDTO dto = buildUserAuthDTO(user);

        return Result.success(dto);
    }

    /**
     * <li>新增用户，同时新增client</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:26:01
     */
    @PostMapping("/c01")
    public Result<UserBaseDTO> addUser(@RequestBody UserBaseDTO dto) {

        String uid = this.JwtUtils.getUserName();
        System.err.println(uid);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.select("id");
        filter.eq("username", dto.getUsername());

        UserBaseModel user = this.service.findOne(filter);
        Assert.isTrue(ObjectUtils.isEmpty(user), "账户名已存在");

        this.service.addUser(dto);

        return Result.success();
    }

    private UserAuthDTO buildUserAuthDTO(UserBaseModel u) {
        UserAuthDTO dto = new UserAuthDTO(u.getId(), u.getUsername(), u.getPassword(), u.getStatus(), u.getByTime());
        u.getRoles().forEach(e -> dto.getRoles().add(e.getCode()));
        return dto;
    }
}
