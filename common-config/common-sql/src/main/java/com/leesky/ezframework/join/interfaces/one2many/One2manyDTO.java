/*
 * @作者: 魏来
 * @日期: 2021年8月24日  下午4:27:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.one2many;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class One2manyDTO {

	private Field f;

	private OneToMany one2many;

	public One2manyDTO() {
	}

	public One2manyDTO(Field f, OneToMany one2many) {

		this.f = f;
		this.one2many = one2many;
	}

}
