package com.leesky.ezframework.mybatis.query;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Hump2underline;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryFilter<T> extends QueryWrapper<T> {

	private static final long serialVersionUID = -1998448102575919460L;

	public Map<String, Object> p1 = Maps.newHashMap();// 原始参数值
	public Map<String, Object> p2 = Maps.newHashMap();// 原始参数转换为下划线格式
	public List<String> op = Lists.newArrayList();// 原始参数转下划线且带有操作符号

	private ParamModel param;

	public QueryFilter() {

	}

	public QueryFilter(ParamModel param) {
		if (StringUtils.equals(getSqlSelect(), null))
			this.setSelect("*");

		this.param = param;
		setSelect(param.getExt());
		analyzing(param.getQueryStr(), Lists.newArrayList());
	}

	public QueryFilter(ParamModel param, List<String> remove) {

		this.param = param;
		setSelect(param.getExt());
		analyzing(param.getQueryStr(), remove);

	}

	/**
	 * @作者: 魏来
	 * @日期: 2021/8/18 下午2:05
	 * @描述: 带有排序字段
	 **/
	public QueryFilter(ParamModel param, String orderField, String descAsc) {

		this.param = param;
		setSelect(param.getExt());

		switch (descAsc) {

		case "asc":
			this.orderByAsc(orderField);
			break;

		case "desc":
			this.orderByDesc(orderField);
			break;

		}

		analyzing(param.getQueryStr(), Lists.newArrayList());

	}

	public IPage<T> page() {
		return new Page<>(param.getPage(), param.getLimit());
	}

	private void setSelect(String str) {

		if (StringUtils.isNotBlank(str)) {

			List<String> ret = Lists.newArrayList();

			String[] array = StringUtils.split(str, ",");
			for (String c : array) // @Select 内容是Model属性，所以用驼峰命名规则转为：数据表字段。当然如果你在@Select直接用数据表字段更好
				ret.add(Hump2underline.build(c));

			this.select(ret.toArray(new String[0]));

		}

	}

	private void analyzing(String str, List<String> remove) {

		if (StringUtils.isNotBlank(str)) {
			Map<String, String> params = JSON.parseObject(str, new TypeReference<>() {
			});

			for (Map.Entry<String, String> map : params.entrySet()) {
				if (remove.contains(map.getKey()))// 如果是需要排除的参数，则跳过，继续下一个循环
					continue;

				if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {

					str = map.getKey().replace("Q_", "");
					String[] array = StringUtils.split(str, "_");
					getOpera(array, map.getValue(), true);

				}

			}

		}

	}

	private void getOpera(String[] array, Object val, Boolean v) {

		String column;

		if (array.length > 1) {
			p1.put(array[0], val);
			p2.put(Hump2underline.build(array[0]), val);

			if (v)
				column = Hump2underline.build(array[0]);
			else
				column = array[0];

			switch (array[1]) {

			case "EQ":
				this.eq(column, val);
				op.add(Hump2underline.build(array[0]) + "='" + val + "'");
				break;

			case "NE":
				this.ne(column, val);
				op.add(Hump2underline.build(array[0]) + " != '" + val + "'");
				break;

			case "GE":
				this.ge(column, val);
				op.add(Hump2underline.build(array[0]) + " >= '" + val + "'");
				break;

			case "GT":
				this.gt(column, val);
				op.add(Hump2underline.build(array[0]) + " > '" + val + "'");
				break;

			case "LE":
				this.le(column, val);
				op.add(Hump2underline.build(array[0]) + " <= '" + val + "'");
				break;

			case "LT":
				this.lt(column, val);
				op.add(Hump2underline.build(array[0]) + " < '" + val + "'");
				break;

			case "LK":
				this.like(column, val);
				op.add(Hump2underline.build(array[0]) + " like '%" + val + "%'");
				break;

			case "LKLeft":
				this.likeLeft(column, val);
				op.add(Hump2underline.build(array[0]) + " like '" + val + "%'");
				break;

			case "LKRight":
				this.likeRight(column, val);
				op.add(Hump2underline.build(array[0]) + " like '%" + val + "'");
				break;

			case "NK":
				this.notLike(column, val);
				op.add(Hump2underline.build(array[0]) + " not like '%" + val + "%'");
				break;

			case "BW":
				String[] va = StringUtils.split(val.toString(), "~");
				this.between(column, va[0], va[1]);
				op.add(Hump2underline.build(array[0]) + " BETWEEN '" + va[0] + "' to '" + va[1] + "'");
				break;

			case "IN":
				this.in(column, (Object) String.valueOf(val).split(","));
				op.add(Hump2underline.build(array[0]) + " in('" + StringUtils.join(Lists.newArrayList(StringUtils.split((String) val, ",")), "','") + "')");
				break;

			}

		}

	}

}
