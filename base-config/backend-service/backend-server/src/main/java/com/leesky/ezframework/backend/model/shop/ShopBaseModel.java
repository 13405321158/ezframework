/*
 * @作者: 魏来
 * @日期: 2022/3/2 下午5:02
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model.shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.backend.mapper.dealer.IdealerExt01Mapper;
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

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/2 下午5:02
 */
@Getter
@Setter
@TableName("cbm_mag_member")
@ApiModel(value = "小B类用户基本信息：既是买家也是卖家")
public class ShopBaseModel extends BaseUuidModel {
    private static final long serialVersionUID = -7296725719916650044L;

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty("登录名")
    private String uname;

    @ApiModelProperty("微信openid")
    private String openId;

    @ApiModelProperty("登录密码")
    private String password;


    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("账户状态")
    private String status;

    @ApiModelProperty("账户有效期至")
    private Date byTime;

    @ApiModelProperty("修改密码时间")
    private Date editPwdDate;

    @ApiModelProperty("扩展表01主键")
    private String ext01Id;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty("审核")
    private String auditStep;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext01_id")
    @EntityMapper(targetMapper = IdealerExt01Mapper.class, entityClass = ShopExt01Model.class)
    private ShopExt01Model ext01;



    @TableField(exist = false)
    private Set<GroupModel> groupSet;

    @ManyToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "user_id")
    @InverseJoinColumn(referencedColumnName = "role_id")
    @EntityMapper(targetMapper = Iuser2roleMapper.class, entityClass = User2RoleModel.class)
    private Set<RoleModel> roles;
}
