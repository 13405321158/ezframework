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
import com.leesky.ezframework.join.interfaces.one2one.One2One;
import com.leesky.ezframework.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Setter
@Getter

@TableName("cbm_mag_user_ext02")
@ApiModel(value = "基本用户扩展表02")
public class UserBaseExt02Model extends BaseUuidModel {

    private static final long serialVersionUID = -2812097282561425555L;
    @ApiModelProperty("登录名")
    @TableField(value = "wx_open_id")
    private String wxOpenId;

    @ApiModelProperty("登录名")
    @TableField(value = "ios_userid")
    private String iosId;

    private String ordersn;

    @TableField(exist = false)
    @One2One(otherOneTableName = "cbm_mag_user", otherOneTableColumn = "ext02_id")
    private UserBaseModel userBaseModel;

    public UserBaseExt02Model() {
        this.wxOpenId = RandomStringUtils.randomAlphanumeric(15);
        this.iosId = RandomStringUtils.randomAlphanumeric(5);
        this.ordersn = RandomStringUtils.randomAlphanumeric(30);
    }
}
