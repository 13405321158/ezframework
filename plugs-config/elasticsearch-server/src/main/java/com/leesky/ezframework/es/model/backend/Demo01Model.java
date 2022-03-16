/*
 * @作者: 魏来
 * @日期: 2021/6/2  下午1:55
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.model.backend;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Document(indexName = "demo01",createIndex = false)

public class Demo01Model implements Serializable {

	private static final long serialVersionUID = 5099018397552685698L;

	@Id
	private String id;

	// 说明：title属性 支持模糊查询，或者说支持分词搜索; 而@Field(type = FieldType.Keyword) 不支持模糊查询
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String title;

	@Field(type = FieldType.Double)
	private BigDecimal price;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private List<String> tag;

	public Demo01Model() {
		if (StringUtils.isBlank(this.id)) {
			this.id = UUID.randomUUID().toString();
		}
	}

	public Demo01Model(String title, BigDecimal price, List<String> tag) {
		this.title = title;
		this.price = price;
		this.tag = tag;
	}
}
