/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.one2one.OneToOne;

import lombok.Data;

@Data
public class MappingUtils<T> {

	private T entity;

	private BaseMapper<T> baseMapper;

	public List<WaitSaveEntityDTO> waits = Lists.newArrayList();// 保存待存储待实体

	/**
	 * @author:weilai
	 * @Data:2020-8-1910:44:25
	 * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
	 */
	public void relationship(T entity, BaseMapper<T> baseMapper) {
		this.entity = entity;
		this.baseMapper = baseMapper;

		List<Field> fields = JoinUtil.getAllField(entity);

		for (Field f : fields) {
			OneToOne one2one = f.getAnnotation(OneToOne.class);
//			ManyToOne many2one = f.getAnnotation(ManyToOne.class);
//			OneToMany one2many = f.getAnnotation(OneToMany.class);
//			ManyToMany many2many = f.getAnnotation(ManyToMany.class);

			if (ObjectUtils.isNotEmpty(one2one)) {
				String relationField = one2one.relationField();
				if (StringUtils.isBlank(relationField))
					waits.add(new WaitSaveEntityDTO(f, entity, one2one.joinColumn()));
				else
					JoinUtil.setValue(entity, relationField, one2oneHandler(f, entity, one2one));
			}
//			if (ObjectUtils.isNotEmpty(many2one))
//			if (ObjectUtils.isNotEmpty(one2many))
//			if (ObjectUtils.isNotEmpty(many2many))
		}
		this.baseMapper.insert(entity);

		Object key = JoinUtil.getId(entity);
		waits.forEach(e -> e.save(key));
		waits.clear();
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月23日 下午5:29:48
	 * @描述: 存储实体中的关系对象，适用于：one2one
	 */
	private Object one2oneHandler(Field f, Object entity, OneToOne one2one) {
		Object key = null;

		Object obj = new WaitSaveEntityDTO(f, entity, one2one.relationField()).save();

		if (StringUtils.equals("id", one2one.joinColumn()))// 主键关联，返回子表主键
			key = JoinUtil.getId(obj);
		else
			key = JoinUtil.getValue(obj, one2one.joinColumn());// 非主键关联，返回指定的关联字段值

		return key;
	}



}
