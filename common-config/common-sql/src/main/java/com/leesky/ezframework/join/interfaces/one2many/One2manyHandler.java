/*
 * @作者: 魏来
 * @日期: 2021/8/27  上午11:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.one2many;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.service.IbaseService;

import lombok.Data;

@Data
@Component
@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
public class One2manyHandler {

	private Field f;

	private Object entity;

	private String joinField;

	@Autowired
	private SpringContextHolder springContextHolder;

	public One2manyHandler build(Field f, Object entity, String joinField) {

		this.f = f;
		this.entity = entity;
		this.joinField = joinField;

		f.setAccessible(true);

		return this;
	}

	public void save(Object v) {

		try {
			Object obj = this.f.get(entity);
			String beanName = JoinUtil.buildServiceBeanNaem(this.f);
			IbaseService service = (IbaseService) this.springContextHolder.getBean(beanName);

			if (obj instanceof List) {
				for (Object o : (List) obj) {
					BeanUtils.setProperty(o, this.joinField, v);
					service.insert(o, false);
				}
			}

		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
