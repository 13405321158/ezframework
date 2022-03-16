/*
 * @作者: 魏来
 * @日期: 2021/6/2  下午1:55
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@Document(indexName = "demo02",createIndex = false)
public class Demo02Model {

    @Id
    private String id;

    //说明title属性 支持模糊查询，或者说支持分词搜索; 而@Field(type = FieldType.Keyword) 不支持模糊查询
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private BigDecimal price;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private List<String> tag;

    public Demo02Model() {
        if (StringUtils.isBlank(this.id)) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
