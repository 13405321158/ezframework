package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leesky.ezframework.backend.dto.RoleDTO;
import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.backend.model.User2RoleModel;
import com.leesky.ezframework.backend.service.IroleService;
import com.leesky.ezframework.backend.service.Iuser2roleService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.CommonDTO;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色操作控制器
 *
 * @author： 魏来
 * @date： 2021/12/10 下午1:45
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleAction {

    private final IroleService service;
    private final Iuser2roleService user2roleService;

    /**
     * 分页显示角色信息
     *
     * @author： 魏来
     * @date: 2021/12/10 下午3:09
     */
    @GetMapping("/r01")
    public Result<List<RoleDTO>> list(@RequestBody ParamModel model) {

        QueryFilter<RoleModel> filter = new QueryFilter<>(model);
        Page<RoleModel> data = this.service.page(filter);

        List<RoleDTO> ret = Po2DtoUtil.convertor(data.getRecords(), RoleDTO.class);

        return Result.success(ret, data.getTotal());
    }

    /**
     * 增加角色：纯粹增加角色，不涉及与角色关联关系，如用户和角色，角色和资源
     *
     * @author： 魏来
     * @date: 2021/12/10 下午2:45
     */
    @PostMapping("/c01")
    public Result<?> addRole01(@RequestBody RoleDTO dto) {

        RoleModel model = Po2DtoUtil.convertor(dto, RoleModel.class);
        this.service.insert(model, false);
        return Result.success();
    }

    /**
     * 增加角色和用户关联关系：参数pid=角色id，cid=用户id
     *
     * @author： 魏来
     * @date: 2021/12/10 下午2:45
     */
    @PostMapping("/c02")
    public Result<?> addRole02(@RequestBody CommonDTO dto) {
        Assert.isTrue(StringUtils.isNotBlank(dto.getPid()), "增加或修改【用户-角色】关联关系时，角色id不能是空值");
        Assert.isTrue(CollectionUtils.isNotEmpty(dto.getCid()), "增加或修改【用户-角色】关联关系时，没有选择任何用户");

        this.user2roleService.addUser2role(dto);

        return Result.success();
    }

    /**
     * 增加角色
     *
     * @author： 魏来
     * @date: 2021/12/10 下午2:45
     */
    @PostMapping("/c03")
    public Result<RoleModel> editRole(@RequestBody RoleDTO dto) {

        Assert.isTrue(StringUtils.isNotBlank(dto.getId()), "修改角色信息参数Id不能是空值");
        UpdateWrapper<RoleModel> filter = new UpdateWrapper<>();
        filter.eq("id", dto.getId());

        if (StringUtils.isNotBlank(dto.getName())) {
            filter.set("name", dto.getName());
        }
        if (ObjectUtils.isNotEmpty(dto.getSortNo())) {
            filter.set("sort_no", dto.getSortNo());
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            filter.set("description", dto.getDescription());
        }

        this.service.update(filter);

        return Result.success();
    }

    /**
     * 删除角色，同时删除：用户和角色对应表、角色和资源对应表
     *
     * @author： 魏来
     * @date: 2021/12/10 下午3:01
     */
    public Result<?> delRole(@RequestBody RoleDTO dto) {

        Assert.isTrue(StringUtils.isNotBlank(dto.getId()), "删除角色信息参数Id不能是空值");

        //1、删除用户和角色对应关系表
        QueryFilter<User2RoleModel> ur = new QueryFilter<>();
        ur.eq("user_id", dto.getId());
        this.user2roleService.delete(ur);

        //2、删除角色和资源对应关系表
        //TODO;

        //最后删除 角色信息
        this.service.delete(dto.getId());
        return Result.success();
    }
}
