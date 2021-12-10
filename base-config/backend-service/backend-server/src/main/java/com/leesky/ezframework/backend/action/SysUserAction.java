package com.leesky.ezframework.backend.action;

import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.Po2DtoUtil;
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

    /**
     * <li>登录获取token时使用</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:05:39
     */
    @GetMapping("/{username}/public")
    public Result<UserAuthDTO> getUserByUsername(@PathVariable String username) {


        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.eq("username", username);
        filter.select("id,username,status,by_time,password");
        UserBaseModel user = this.service.findOne(filter);

        UserAuthDTO dto = Po2DtoUtil.convertor(user, UserAuthDTO.class);


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

//        JwtUtils.getRoles();

        QueryFilter<UserBaseModel> filter = new QueryFilter<>();
        filter.eq("username", dto.getUsername());
        filter.select("id");
        UserBaseModel user = this.service.findOne(filter);
        Assert.isTrue(ObjectUtils.isEmpty(user), "账户名已存在");

        this.service.addUser(dto);

        return Result.success();
    }
}
