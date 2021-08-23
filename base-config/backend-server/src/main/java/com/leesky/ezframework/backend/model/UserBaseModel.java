/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:08
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.join.interfaces.OneToOne;
import com.leesky.ezframework.model.BaseUuidModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_mag_user1")
@ApiModel(value = "基本用户信息")
public class UserBaseModel extends BaseUuidModel {

	private static final long serialVersionUID = 8547824568011487339L;
	@ApiModelProperty("登录名")
	private String username = "weilai";

	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("随机数")
	private String randomKey = "leesky09";

	@ApiModelProperty("修改密码时间")
	private Date editPwdDate = new Date();

	@ApiModelProperty("扩展表01主键")
	private String ext01Id;

	@ApiModelProperty("扩展表02主键")
	private String ext02Id;

	@TableField(exist = false)
	@OneToOne(otherOneTableName = "cbm_mag_user1_ext01", serviceName = "iuserBaseExt01Service")
	private UserBaseExt01Model ext01;

	@TableField(exist = false)
	@OneToOne(otherOneTableName = "cbm_mag_user1_ext02", serviceName = "iuserBaseExt02Service")
	private UserBaseExt02Model ext02;

	public UserBaseModel() {
	}

	public UserBaseModel(UserBaseExt01Model ext01, UserBaseExt02Model ext02) {
		this.ext01 = ext01;
		this.ext02 = ext02;
	}
}
