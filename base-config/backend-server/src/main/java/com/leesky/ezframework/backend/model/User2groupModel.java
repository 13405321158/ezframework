/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午2:49:00
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_mag_group_user")
@ApiModel(value = "用户与群组 中间表")
public class User2groupModel implements Serializable{


	private static final long serialVersionUID = -6935710252282544978L;

	@ApiModelProperty("用户id")
	private String userId;
	
	@ApiModelProperty("群组id")
	private String groupId;
}
