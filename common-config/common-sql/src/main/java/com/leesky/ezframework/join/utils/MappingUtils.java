/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.one2one.OneToOne;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class MappingUtils<T> {

	private T entity;

	private BaseMapper<T> baseMapper;

	public List<WaitSaveEntityDTO> waits = Lists.newArrayList();// 保存待存储待实体

	/**
	 * @author:weilai
	 * @Data:2020-8-1910:44:25
	 * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
	 * @Desc: 关联关系 分为主表和从表，含义参见各自类说明
	 * @Desc：先存储从表后存储主表，因为存储完毕后实体类才有主键，才能把得到到主键 赋值给 主表中对应字段（非主键关联谁先谁后存储，无所谓）
	 */
	public void relationship(T entity, BaseMapper<T> baseMapper) {
		this.entity = entity;
		this.baseMapper = baseMapper;

		// 1、查找出当前实体entity中的所有字段
		List<Field> fields = JoinUtil.getAllField(entity);

		// 2、遍历字段，找出：one2one、many2many、many2one、one2many 关系
		for (Field f : fields) {

//			ManyToOne many2one = f.getAnnotation(ManyToOne.class);
//			OneToMany one2many = f.getAnnotation(OneToMany.class);
//			ManyToMany many2many = f.getAnnotation(ManyToMany.class);

			// 2.1 one2one关系
			OneToOne one2one = f.getAnnotation(OneToOne.class);
			if (ObjectUtils.isNotEmpty(one2one)) {
				String rf = one2one.relationField();
				if (StringUtils.isBlank(rf))
					waits.add(new WaitSaveEntityDTO(f, entity, one2one.joinColumn()));// f.get(entity)是主表，先保存起来，遍历完毕再存储
				else
					JoinUtil.setValue(entity, rf, one2oneHandler(f, entity, one2one));// f.get(entity)是从表，立刻存储，获取关联关系到主键，并赋值给主表
			}
//			if (ObjectUtils.isNotEmpty(many2one))
//			if (ObjectUtils.isNotEmpty(one2many))
//			if (ObjectUtils.isNotEmpty(many2many))
		}
		this.baseMapper.insert(entity);

		Object key = JoinUtil.getId(entity);
		waits.forEach(e -> e.save(key));// 如果waits不是空，则说明enity 对于某个关系来说 是从表
		waits.clear();
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月23日 下午5:29:48
	 * @描述: 存储实体中的关系对象，适用于：one2one
	 */
	private Object one2oneHandler(Field f, Object entity, OneToOne one2one) {
		Object key;

		Object obj = new WaitSaveEntityDTO(f, entity, one2one.relationField()).save();

		if (StringUtils.equals("id", one2one.joinColumn()))// 主键关联，返回子表主键
			key = JoinUtil.getId(obj);
		else
			key = JoinUtil.getValue(obj, one2one.joinColumn());// 非主键关联，返回指定的关联字段值

		return key;
	}

}
