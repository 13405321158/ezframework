/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:11
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.IuserBaseMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@TableName("user_sys_ext02")
@ApiModel(value = "平台用户扩展信息02")
public class UserBaseExt02Model extends BaseUuidModel {

    private static final long serialVersionUID = -2812097282561425555L;
    @ApiModelProperty("登录名")
    @TableField(value = "wx_open_id")
    private String wxOpenId;

    @ApiModelProperty("登录名")
    @TableField(value = "ios_userid")
    private String iosId;

    @ApiModelProperty("用户id")
    private String userId;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "user_id")
    @EntityMapper(targetMapper = IuserBaseMapper.class, entityClass = UserBaseModel.class)
    private UserBaseModel userBaseModel;


}
