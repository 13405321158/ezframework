package com.leesky.ezframework.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

@JsonInclude(value = Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamModel {
	private String ext;// 扩展参数
	private Integer page;// 页码
	private Integer limit;// 每页数量
	private String order;// 排序字段
	private String queryStr;// 查询语句

	private List<Object> list = Lists.newArrayList();// 扩展参数

	public ParamModel() {
		this.page = 0;
		this.limit = 10;

	}

	/**
	 * @author: weilai
	 * @date: 2021/5/20 上午10:42
	 * @desc: 手动增加查询条件
	 **/
	public void addFilter(Map<String, String> queryMap) {

		Map<String,String> map = JSON.parseObject(this.queryStr, new TypeReference<>(Map.class) {
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

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
}
