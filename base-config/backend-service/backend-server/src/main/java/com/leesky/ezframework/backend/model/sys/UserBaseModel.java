/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:08
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.backend.mapper.sys.IuserBaseExt01Mapper;
import com.leesky.ezframework.backend.mapper.sys.IuserBaseExt02Mapper;
import com.leesky.ezframework.backend.model.GroupModel;
import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.backend.model.User2RoleModel;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@TableName("cbm_mag_user")
@ApiModel(value = "基本用户信息")
public class UserBaseModel extends BaseUuidModel {

    private static final long serialVersionUID = 3074985087761682444L;

    @ApiModelProperty("登录名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("账户状态")
    private String status;

    @ApiModelProperty("账户有效期至")
    private Date byTime;

    @ApiModelProperty("修改密码时间")
    private Date editPwdDate;

    @ApiModelProperty("扩展表01主键")
    private String ext01Id;

    @ApiModelProperty("扩展表02主键")
    private String ext02Id;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty("审核")
    private String auditStep;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext01_id")
    @EntityMapper(targetMapper = IuserBaseExt01Mapper.class, entityClass = UserBaseExt01Model.class)
    private UserBaseExt01Model ext01;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext02_id")
    @EntityMapper(targetMapper = IuserBaseExt02Mapper.class, entityClass = UserBaseExt02Model.class)
    private UserBaseExt02Model ext02;

    @TableField(exist = false)
    private Set<GroupModel> groupSet;

    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "user_id")
    @InverseJoinColumn(referencedColumnName = "role_id")
    @EntityMapper(targetMapper = Iuser2roleMapper.class, entityClass = User2RoleModel.class)
    private Set<RoleModel> roles;


}
