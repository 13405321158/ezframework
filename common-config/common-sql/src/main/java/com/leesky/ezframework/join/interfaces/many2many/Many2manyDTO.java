/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午4:43:10
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2many;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class Many2manyDTO {

	private String field01;// 字段01

	private String field02;// 字段02

	private List<Object> v01;// 字段01的值

	private List<Object> v02;// 字段02的值

	private String tableName;// 表名称

	private Boolean flage = false;

	private String targetField;//插入前删除条件字段
	private Object targetValue;//插入前删除条件内容

	public Many2manyDTO() {

	}

	public Many2manyDTO(Many2Many flag, List<Object> v) {

		if (StringUtils.equals(flag.order(), "1")) {
			this.flage = true;

			this.v01 = v;
			this.field02 = flag.middleTableColumn();
			this.field01 = flag.otherMiddleTableColumn();
		} else {
			this.v02 = v;
			this.field01 = flag.middleTableColumn();
			this.field02 = flag.otherMiddleTableColumn();
		}

		this.tableName = flag.middleTableName();
	}

	public void build(Object v) {
		this.targetValue =v;
		List<Object> ret = Lists.newArrayList();
		if (flage) {
			this.v02 = ret;
			this.targetField = field02;
			this.v01.forEach(e -> ret.add(v));
		} else {
			this.v01 = ret;
			this.targetField = field01;
			this.v02.forEach(e -> ret.add(v));
		}
	}
}
