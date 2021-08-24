/*
 * @作者: 魏来
 * @日期: 2021年8月24日  下午4:50:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.one2one.one2oneDTO;

/**
 * @author leesky
 * @desc:
 *        <li>我把one2one 关系 划分为主表和从表；
 *        <li>含有另一个表关系数据则称之为：主表
 *        <li>同理,不含有的则称之为：从表
 *        <li>双方表相互含有对方关系，则这两个表都是主表
 */
@Component
public class One2oneHandler<T> {

	private T entity;

	private BaseMapper<T> baseMapper;

	private List<one2oneDTO> one2oneList;

	public void build(T entity, BaseMapper<T> baseMapper, List<one2oneDTO> one2oneList) {

		this.entity = entity;
		this.baseMapper = baseMapper;
		this.one2oneList = one2oneList;
	}

	public void handler() {

		List<one2oneDTO> main = Lists.newArrayList();
		List<one2oneDTO> item = Lists.newArrayList();

		// 1、以 entity 为参照物，区分开 主表和 从表；
		for (one2oneDTO o : one2oneList) {
			if (StringUtils.isBlank(o.getOne2one().joinColumn()))
				item.add(o);
			else
				main.add(o);
		}

		// 2、从表先存储，获取id或者关联关系数据后 赋值给主表 entity
		for (one2oneDTO o : item) {
		}

		// 3、 entity作为 主表存储，此时带有 从表的关系数据
		this.baseMapper.insert(entity);

		// 4、main相对于 entity是 主表，开始存储

	}

}
