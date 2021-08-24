/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.many2many.Many2manyDTO;
import com.leesky.ezframework.join.interfaces.many2many.ManyToMany;
import com.leesky.ezframework.join.interfaces.many2one.Many2oneDTO;
import com.leesky.ezframework.join.interfaces.many2one.ManyToOne;
import com.leesky.ezframework.join.interfaces.one2many.One2manyDTO;
import com.leesky.ezframework.join.interfaces.one2many.OneToMany;
import com.leesky.ezframework.join.interfaces.one2one.OneToOne;
import com.leesky.ezframework.join.interfaces.one2one.one2oneDTO;
import com.leesky.ezframework.model.BaseAutoModel;
import com.leesky.ezframework.model.BaseUuidModel;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MappingUtils<T> {

	private T entity;

	private BaseMapper<T> baseMapper;

	private final One2oneHandler<T> one2oneHandler;
	
	private final SpringContextHolder springContextHolder;

	
	private List<one2oneDTO> one2oneList = Lists.newArrayList();

	private List<Many2manyDTO> many2manyList = Lists.newArrayList();

	private List<Many2oneDTO> many2oneList = Lists.newArrayList();

	private List<One2manyDTO> one2manyList = Lists.newArrayList();

	/**
	 * @author:weilai
	 * @Data:2020-8-1910:44:25
	 * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
	 */
	public void relationship(T entity, BaseMapper<T> baseMapper) {
		this.entity = entity;
		this.baseMapper = baseMapper;

		List<Field> fields = getAllField(entity);

		for (Field f : fields) {
			OneToOne one2one = f.getAnnotation(OneToOne.class);
			if (ObjectUtils.isNotEmpty(one2one))
				one2oneList.add(new one2oneDTO(f, one2one));
//				Object key = saveAction(f, entity, one2one);
//				if (StringUtils.isNotBlank(one2one.relationField()))
//					// 说明entity是one2one关系的主表(主表最后存储，存储时子表的关系值也能够得到)；
//					// saveAction动作是 存储从表，所以把从表中的关联关系字段值赋给主表
//					setValue(entity, one2one.relationField(), key);
//				else {// 说明entity是one2one关系的从表(子表最后存储，主表先存储时得不到关联关系值)
//
//				}

			ManyToOne many2one = f.getAnnotation(ManyToOne.class);
			if (ObjectUtils.isNotEmpty(many2one))
				many2oneList.add(new Many2oneDTO(f, many2one));

			OneToMany one2many = f.getAnnotation(OneToMany.class);
			if (ObjectUtils.isNotEmpty(one2many))
				one2manyList.add(new One2manyDTO(f, one2many));

			ManyToMany many2many = f.getAnnotation(ManyToMany.class);
			if (ObjectUtils.isNotEmpty(many2many))
				many2manyList.add(new Many2manyDTO(f, many2many));
		}
//		this.baseMapper.insert(entity);

	}

	public void handler() {
		one2oneHandler.build(entity, baseMapper,one2oneList);
	}
	/**
	 * @author: weilai
	 * @Data:2021年1月30日下午3:15:20
	 * @Desc:获取类所有属性，包括父类，爷爷等类
	 */
	private List<Field> getAllField(Object model) {
		Class<?> clazz = model.getClass();
		List<Field> fields = Lists.newArrayList();
		while (clazz != null) {
			Field[] fs = clazz.getDeclaredFields();
			fields.addAll(Arrays.asList(fs));

			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月23日 下午5:29:48
	 * @描述: 存储实体中的关系对象，适用于：one2one
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	private Object saveAction(Field f, Object entity, OneToOne one2one) {
		Object key = null;
		try {
			f.setAccessible(true);
			Object obj = f.get(entity);

			String beanName = buildMapperBeanName(f);
			BaseMapper iMapper = (BaseMapper) springContextHolder.getBean(beanName);
			iMapper.insert(obj);

			if (StringUtils.equals("id", one2one.joinColumn()))// 主键关联，返回子表主键
				key = getId(obj);
			else
				key = getValue(obj, one2one.joinColumn());// 非主键关联，返回指定的关联字段值

		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.getMessage();
		}
		return key;
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月24日 上午8:27:22
	 * @描述: 根据model实体名称 构造对应到ixxxMapper名称
	 */
	private String buildMapperBeanName(Field f) {
		String modelName = StringUtils.substringAfterLast(f.getType().getName(), ".").replace("Model", "");
		return "i" + StringUtils.uncapitalize(modelName) + "Mapper";
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月24日 上午10:27:17
	 * @描述: 获取记录id
	 */
	private String getId(Object obj) {
		String key = "";
		if (obj instanceof BaseUuidModel) {
			BaseUuidModel model = ((BaseUuidModel) obj);
			key = model.getId();
		} else if (obj instanceof BaseAutoModel) {
			BaseAutoModel model = ((BaseAutoModel) obj);
			key = String.valueOf(model.getId());
		}

		return key;
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月24日 下午12:26:39
	 * @描述: 向字段赋值: entity.setFiled(v)
	 */
	private void setValue(T entity, String filed, Object v) {
		try {
			BeanUtils.setProperty(entity, Hump2underline.lineToHump(filed), v);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.getMessage();
		}
	}

	private Object getValue(Object model, String filed) {
		Object ret = null;
		try {
			ret = BeanUtils.getProperty(model, filed);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.getMessage();
		}
		return ret;
	}

//	private String getMethod(String filed) {// bean 属性的 get方法
//		String beanPropertyName = Hump2underline.lineToHump(filed);
//		return "get" + StringUtils.capitalize(beanPropertyName);
//	}
//
//	private String setMethod(String filed) {// bean 属性的 set方法
//		String beanPropertyName = Hump2underline.lineToHump(filed);
//		return "set" + StringUtils.capitalize(beanPropertyName);
//	}
}
