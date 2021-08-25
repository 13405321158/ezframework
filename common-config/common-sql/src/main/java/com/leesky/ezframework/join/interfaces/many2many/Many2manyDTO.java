/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午4:43:10
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2many;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class Many2manyDTO {

	private Object v01;

	private Object v02;

	private String field01;

	private String field02;

	private String tableName;

	private Boolean flage = false;

	public Many2manyDTO() {

	}

	public Many2manyDTO(ManyToMany flag, Object v) {

		if (StringUtils.equals(flag.order(), "1")) {
			this.flage = true;

			this.v01 = v;
			this.field01 = flag.middleTableColumn();
			this.field02 = flag.otherMiddleTableColumn();
		} else {
			this.v02 = v;
			this.field02 = flag.middleTableColumn();
			this.field01 = flag.otherMiddleTableColumn();
		}

	}

	public void build(Object v) {

		if (flage)
			this.v02 = v;
		else {
			this.v01 = v;
		}
	}
}
