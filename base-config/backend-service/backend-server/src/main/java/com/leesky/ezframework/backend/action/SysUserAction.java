package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.backend.vo.UserBaseVO;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leesky.ezframework.json.Result.success;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserAction {

    private final IuserBaseService service;

    /**
     * <li>登录获取token时使用</li>
     *
     * @author: 魏来
     * @date: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{username}/public")
    public Result<UserAuthDTO> getUserByUsername(@PathVariable String username) {

        QueryFilter<UserBaseModel> filter = new QueryFilter<>(ImmutableMap.of("Q_username_EQ", username));

        filter.select("id,username,status,by_time,password");

        UserBaseModel user = this.service.findOne(filter, ImmutableMap.of("roles", "code"));

        UserAuthDTO dto = buildUserAuthDTO(user);

        return success(dto, false);
    }

    /**
     * 系统用户列表
     *
     * @author： 魏来
     * @date: 2022/1/7 下午3:31
     */
    @PostMapping(value = "/r01")
    public Result<List<UserBaseVO>> r01(@RequestBody ParamModel param) {
        QueryFilter<UserBaseModel> filter = new QueryFilter<>(param);
        filter.select("id,username,status,ext01.idCard");
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
    public Result<UserBaseDTO> addUser(@RequestBody UserBaseDTO dto) throws Exception {
        ValidatorUtils.valid(dto);

        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.select("id").eq("username", dto.getUsername());

        UserBaseModel user = this.service.findOne(filter);
        Assert.isTrue(ObjectUtils.isEmpty(user), "账户名已存在");

        this.service.addUser(dto);

        return Result.success();
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

    private UserAuthDTO buildUserAuthDTO(UserBaseModel u) {
        UserAuthDTO dto = new UserAuthDTO(u.getId(), u.getUsername(), u.getPassword(), u.getStatus(), u.getByTime());
        u.getRoles().forEach(e -> dto.getRoles().add(e.getCode()));
        return dto;
    }

}
