/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.ManyToMany;
import com.leesky.ezframework.join.interfaces.ManyToOne;
import com.leesky.ezframework.join.interfaces.OneToMany;
import com.leesky.ezframework.join.interfaces.OneToOne;
import com.leesky.ezframework.model.BaseAutoModel;
import com.leesky.ezframework.model.BaseUuidModel;

@Component
public class MappingUtils {

	@Autowired
	private SpringContextHolder springContextHolder;

	/**
	 * @author:weilai
	 * @Data:2020-8-1910:44:25
	 * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
	 */

	public void relationship(Object entity) {
		List<Field> fields = getAllField(entity);

		for (Field f : fields) {
			OneToOne one2one = f.getAnnotation(OneToOne.class);
			ManyToOne many2one = f.getAnnotation(ManyToOne.class);
			OneToMany one2many = f.getAnnotation(OneToMany.class);
			ManyToMany many2many = f.getAnnotation(ManyToMany.class);

			if (ObjectUtils.isNotEmpty(one2one)) {
				saveObject(f, entity);
			}
			if (ObjectUtils.isNotEmpty(many2one)) {
				System.err.println(many2one.oneTableName());
			}
			if (ObjectUtils.isNotEmpty(one2many)) {
				System.err.println(one2many.manyTableName());
			}
			if (ObjectUtils.isNotEmpty(many2many)) {
				System.err.println(many2many.otherManytableName());
			}
		}

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
	private String saveObject(Field f, Object entity) {
		String key = null;
		try {
			Date now = new Date();
			f.setAccessible(true);
			Object obj = f.get(entity);

			BaseMapper iMapper = (BaseMapper) springContextHolder.getBean(buildMapperName(f));

			if (obj instanceof BaseUuidModel) {
				BaseUuidModel model = ((BaseUuidModel) obj);
				model.setCreateDate(now);
				model.setModifyDate(now);
				key = model.getId();

			} else if (obj instanceof BaseAutoModel) {
				BaseAutoModel model = ((BaseAutoModel) obj);
				model.setCreateDate(now);
				model.setModifyDate(now);
				key = model.getId();
			}

			iMapper.insert(obj);
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
	private String buildMapperName(Field f) {
		String modelName = StringUtils.substringAfterLast(f.getType().getName(), ".").replace("Model", "");

		return "i" + StringUtils.uncapitalize(modelName) + "Mapper";
	}
}
