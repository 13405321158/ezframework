/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:08
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
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

    private static final long serialVersionUID = 8547824568011487339L;
    @ApiModelProperty("登录名")
    private String username;

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

    @TableField(exist = false)
//    @One2One(otherOneTableName = "cbm_mag_user_ext01", joinField = "ext01Id")
    private UserBaseExt01Model ext01;

    @TableField(exist = false)
//    @One2One(otherOneTableName = "cbm_mag_user_ext02", joinField = "ext02Id")
    private UserBaseExt02Model ext02;

    @TableField(exist = false)
//    @Many2Many(middleTableName = "cbm_mag_l_group_user", middleTableColumn = "user_id", otherMiddleTableColumn = "group_id", otherTableName = "cbm_mag_group")
    private Set<GroupModel> groupSet;

    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "user_id")
    @InverseJoinColumn(referencedColumnName = "role_id")
    @EntityMapper(targetMapper = Iuser2roleMapper.class, entityClass = User2RoleModel.class)
    private Set<RoleModel> roles;


}
