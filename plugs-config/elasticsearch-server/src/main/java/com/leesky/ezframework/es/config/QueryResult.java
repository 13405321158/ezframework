/*
 * @作者: 魏来
 * @日期: 2021/5/31  下午4:29
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryResult {

    private long total;

    private List<Map<String, Object>> content;

    public QueryResult() {
    }

    public QueryResult(Long total, List<Map<String, Object>> content) {
        this.total = total;
        this.content = content;
    }

}
