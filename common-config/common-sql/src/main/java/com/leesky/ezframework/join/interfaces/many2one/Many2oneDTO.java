/*
 * @作者: 魏来
 * @日期: 2021年8月24日  下午4:25:57
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2one;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class Many2oneDTO {

	private Field f;

	private ManyToOne many2one;

	public Many2oneDTO() {
	}

	public Many2oneDTO(Field f, ManyToOne many2one) {

		this.f = f;
		this.many2one = many2one;
	}
}
