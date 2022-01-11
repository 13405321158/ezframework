package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * desc TODO
 *
 * @author 魏来
 * @date 2021/12/1 下午6:39
 */


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserAction {

    private final JwtUtils JwtUtils;
    private final IuserBaseService service;

    /**
     * <li>登录获取token时使用</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{username}/public")
    public Result<UserAuthDTO> getUserByUsername(@PathVariable String username) {

        QueryFilter<UserBaseModel> filter = new QueryFilter<>(ImmutableMap.of("Q_username_EQ", username));

        filter.select("id,username,status,by_time,password");

        UserBaseModel user = this.service.findOne(filter, ImmutableMap.of("roles", "code"));

        UserAuthDTO dto = buildUserAuthDTO(user);

        return Result.success(dto);
    }

    /**
     * 系统用户列表
     *
     * @author： 魏来
     * @date: 2022/1/7 下午3:31
     */
    @PostMapping(value = "/r01")
    public Result<List<UserBaseDTO>> r01(@RequestBody ParamModel params) {
        QueryFilter<UserBaseModel> filter = new QueryFilter<>(params);

        Page<UserBaseDTO> data = this.service.page(filter, UserBaseDTO.class);

        return Result.success(data.getRecords(), data.getTotal());
    }

    /**
     * <li>新增用户，同时新增client</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:26:01
     */
    @PostMapping("/c01")
    public Result<UserBaseDTO> addUser(@RequestBody UserBaseDTO dto) {

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
