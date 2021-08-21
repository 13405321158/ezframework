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
import com.leesky.ezframework.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter


@TableName("cbm_mag_user1_ext02")
@ApiModel(value = "基本用户扩展表02")
public class UserBaseExt02Model extends BaseUuidModel {

    private static final long serialVersionUID = -2812097282561425555L;
    @ApiModelProperty("登录名")
    @TableField(value = "wx_open_id")
    private String wxOpenId="wx_isods23sadasdfasdf";

    @ApiModelProperty("登录名")
    @TableField(value = "ios_userid")
    private String iosId="ios-asdasdfasd";
}
