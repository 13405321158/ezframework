/*
 * @作者: 魏来
 * @日期: 2021年8月24日  下午4:16:19
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.one2one;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class one2oneDTO {

	private Field f;

	private OneToOne one2one;


	public one2oneDTO() {

	}
	
	public one2oneDTO(Field f, OneToOne one2one) {
		this.f = f;
		this.one2one = one2one;
	}

	
}
