package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.backend.model.sys.UserBaseModel;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/10 下午1:15
 */
@Getter
@Setter
@TableName("cbm_mag_role")
@ApiModel(value = "角色信息表")
public class RoleModel extends BaseUuidModel {

    private static final long serialVersionUID = 1254124897874279216L;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色代码,用在授权时使用 Role_")
    private String code;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("角色类型：系统用户使用=sys001,会员用户使用=member001,经销商使用=经销商编码")
    private String type;

    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "role_id")
    @InverseJoinColumn(referencedColumnName = "user_id")
    @EntityMapper(targetMapper = Iuser2roleMapper.class, entityClass = User2RoleModel.class)
    private Set<UserBaseModel> users;
}
