/*
 * @作者: 魏来
 * @日期: 2022/3/3 下午1:28
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.leesky.ezframework.backend.mapper.Iuser2groupMapper;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

/**
 * <li>用户基本信息 通用字段</li>
 *
 * @author: 魏来
 * @date: 2022/3/3 下午1:28
 */
@Getter
@Setter
public class ZbaseModel extends BaseUuidModel {

    private static final long serialVersionUID = -1759587927242195010L;

    @ApiModelProperty("登录名")
    protected String username;

    @ApiModelProperty("身份证上的姓名")
    private String idName;

    @ApiModelProperty("昵称")
    protected String nickName;

    @ApiModelProperty("微信openid")
    private String openId;

    @ApiModelProperty("密码")
    protected String password;

    @ApiModelProperty("性别")
    protected String gender;

    @ApiModelProperty("手机号")
    protected String mobile;

    @ApiModelProperty("账户状态")
    protected String status;

    @ApiModelProperty("账户有效期至")
    protected Date byTime;

    @ApiModelProperty("修改密码时间")
    protected Date editPwdDate;

    @ApiModelProperty("备注")
    protected String remake;

    @ApiModelProperty("头像")
    protected String avatar;

    @ApiModelProperty("审核")
    protected String auditStep;


    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "user_id")
    @InverseJoinColumn(referencedColumnName = "group_id")
    @EntityMapper(targetMapper = Iuser2groupMapper.class, entityClass = User2groupModel.class)
    private Set<GroupModel> groupSet;

    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "user_id")
    @InverseJoinColumn(referencedColumnName = "role_id")
    @EntityMapper(targetMapper = Iuser2roleMapper.class, entityClass = User2RoleModel.class)
    private Set<RoleModel> roles;
}
