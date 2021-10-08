/*
 * @作者: 魏来
 * @日期: 2021年9月25日  下午2:52:23
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 类功能说明：
 * <li></li>
 */
@Data
public class Many2manyDTO {

    private String tableName;

    private String column01;

    private String column02;

    private List<String> v01 = Lists.newArrayList();

    private List<String> v02;

    public Many2manyDTO() {
    }

    public Many2manyDTO(String tableName, String column01, String column02) {
        this.tableName = tableName;
        this.column01 = column01;
        this.column02 = column02;
    }

    public void build(String v1, List<String> v02) {
        v02.forEach(e -> v01.add(v1));
        this.v02 = v02;
    }
}
