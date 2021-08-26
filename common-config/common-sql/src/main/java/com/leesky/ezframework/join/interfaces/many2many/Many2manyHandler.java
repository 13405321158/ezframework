/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.interfaces.many2many;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.service.IbaseService;

import lombok.Data;

@Data
@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
public class Many2manyHandler {

	private Field f;

	private Object key;

	private Object entity;

	private Many2manyDTO dto;


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

	public List<Object> save() {// 存储 另外一方 many 实体
		List<Object> ret = Lists.newArrayList();
		try {
			Object obj = this.f.get(entity);
			String serviceBeanName = JoinUtil.buildServiceBeanNaem(f);
			IbaseService service = (IbaseService) this.springContextHolder.getBean(serviceBeanName);

			for (Object o : (Set) obj) {
				service.insert(o, false);
				ret.add(JoinUtil.getId(o));
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public void save(Object v) {// 存储中间表

		dto.build(v);
		IbaseMapper baseMapper = (IbaseMapper) this.springContextHolder.getBean("ibaseMapper");
		baseMapper.insertM2M(dto);
	}
}
