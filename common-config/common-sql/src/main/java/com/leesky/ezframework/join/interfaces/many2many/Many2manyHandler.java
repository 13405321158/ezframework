/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2many;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.mapper.IbaseMapper;

import lombok.Data;

@Data
@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
public class Many2manyHandler {

	private Field f;

	private Object key;

	private Object entity;

	private Many2manyDTO dto;

	@Autowired
	private IbaseMapper iMapper;


	@Autowired
	private SpringContextHolder springContextHolder;

	public Many2manyHandler() {

	}

	public Many2manyHandler(Many2manyDTO dto) {

		this.dto = dto;
	}

	public Many2manyHandler(Field f, Object entity) {

		this.f = f;
		this.entity = entity;

		f.setAccessible(true);
	}

	public Object save() {// 存储 另外一方 many 实体
		Object obj = null;
		try {
			obj = this.f.get(entity);

			String mapperBeanName = JoinUtil.buildMapperBeanName(this.f);

			BaseMapper iMapper = (BaseMapper) this.springContextHolder.getBean(mapperBeanName);

			iMapper.insert(obj);

		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public void save(Object v) {// 存储中间表

		this.iMapper.insertM2M(dto);
	}
}
