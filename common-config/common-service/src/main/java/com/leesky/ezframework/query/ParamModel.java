package com.leesky.ezframework.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(value = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamModel {

    private Integer page;// 页码
    private Integer limit;// 每页数量
    private String order;// 排序字段
    private String select;// 要查询的字段
    private String queryStr;// 查询条件
    private List<String> join = Lists.newArrayList();//级联查询


    public ParamModel() {
        this.page = 0;
        this.limit = 10;
    }

    public ParamModel(ImmutableMap<String, String> map) {
        this.page = 0;
        this.limit = 10;
        addFilter(map);
    }

    /**
     * 手动增加查询条件
     *
     * @author: weilai
     * @date: 2021/5/20 上午10:42
     **/
    public void addFilter(Map<String, String> queryMap) {

        Map<String, String> map = JSON.parseObject(this.queryStr, new TypeReference<>(Map.class) {
        });

        if (MapUtils.isNotEmpty(map)) {
            map.putAll(queryMap);
            this.queryStr = JSON.toJSONString(map);
        } else {
            this.queryStr = JSON.toJSONString(queryMap);
        }
    }

    /**
     * 根据查询语句的字段key，获取查询语句中的值
     */
    public String getQueryValue(String queryKey) {
        Map<String, String> dd = JSON.parseObject(this.queryStr, new TypeReference<>() {
        });

        if (MapUtils.isNotEmpty(dd)) {
            return dd.get(queryKey);
        }
        return null;
    }

    public Integer getPage() {
        if (this.page <= 0)
            this.page = 1;
        return page - 1;
    }
}
