/*
 * @作者: 魏来
 * @日期: 2021年8月24日  下午4:22:28
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2many;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class Many2manyDTO {

	private Field f;

	private ManyToMany many2many;

	public Many2manyDTO() {
	}

	public Many2manyDTO(Field f, ManyToMany many2many) {

		this.f = f;
		this.many2many = many2many;
	}

}
