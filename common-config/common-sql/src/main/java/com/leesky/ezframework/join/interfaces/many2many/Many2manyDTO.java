/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午4:43:10
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.many2many;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Many2manyDTO {

    private String field01;// 字段01

    private String field02;// 字段02

    private List<Object> v01;// 字段01的值

    private List<Object> v02;// 字段02的值

    private String tableName;// 表名称


    private String targetField;//插入前删除条件字段
    private Object targetValue;//插入前删除条件内容

    public Many2manyDTO() {

    }

    public Many2manyDTO(Many2Many m2m, List<Object> v) {
        this.v01 = v;
        this.tableName = m2m.middleTableName();

        this.field02 = m2m.middleTableColumn();
        this.field01 = m2m.otherMiddleTableColumn();
    }

    public void build(Object v) {
        this.targetValue = v;
        this.targetField = field02;

        this.v02  = Lists.newArrayList();
        this.v01.forEach(e ->  this.v02.add(v));
    }
}
