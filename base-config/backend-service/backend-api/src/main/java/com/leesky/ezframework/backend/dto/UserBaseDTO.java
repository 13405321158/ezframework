/*
 * @作者: 魏来
 * @日期: 2021年12月3日  上午9:16:48
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 类功能说明：
 * <li>新增、编辑、查找账户时使用</li>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBaseDTO {

	// 基本信息表属性 开始
	private String id;

	private String status;

	private String username;

	private String password;

	// 扩展信息01表属性 开始

	// 扩展信息02表属性 开始

	public UserBaseDTO() {
		this.status = "1";//新增用户默认是停用状态，必须审核通过后才启用
	}
}
