/**
 * 
 * @author:weilai
 * @Data:2020-8-1817:30:06
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.join.interfaces.many2may.ManyToMany;
import com.leesky.ezframework.join.interfaces.many2may.ManyToManyDto;
import com.leesky.ezframework.join.interfaces.many2one.ManyToOne;
import com.leesky.ezframework.join.interfaces.one2many.OneToMany;
import com.leesky.ezframework.join.interfaces.one2one.OneToOne;
import com.leesky.ezframework.utils.Hump2underline;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc
 *       <li>设计思路是：先查询 当前实体，然后遍历当前实体内的注解
 *       <li>当前实体即使查询出来是一个实体，也要把他放入list中然后扔进AutoMapper
 *       <li>所以本类有了两层循环：先遍历 T，然后遍历 T内的 Field
 *       <li>selectFields是查询子表sql的 select 部分; 支持多个如：selectFields="id,name,sex"
 */
@Component
@SuppressWarnings("unchecked")
public class AutoMapper<T, M> {

	private static ConcurrentHashMap<String, String> selectField = new ConcurrentHashMap<>();

	private LeeskyMapper<T> baseMapper = null;

	public List<T> mapper(List<T> list, M repo, String selectFields) {
		baseMapper = (LeeskyMapper<T>) repo;

		for (T t : list) {// 1、遍历所有类，

			List<Field> fields = getAllField(t);

			String foreignKeyPropertyValue = getTableIdPropertyValue(fields, t);// 查找 主键值，根据此值获取关联数据

			for (Field f : fields) {// 2、遍历每个类上面的注解

				ManyToMany many2many = f.getAnnotation(ManyToMany.class);// 查找当前字段注解 = ManyToMany ?
				if (ObjectUtils.isNotEmpty(many2many)) {
					handle_Many2Many(many2many, foreignKeyPropertyValue, t, f);
					continue;
				}

				ManyToOne many2one = f.getAnnotation(ManyToOne.class);// 查找当前字段注解= ManyToOne ?
				if (ObjectUtils.isNotEmpty(many2one)) {
					handle_ManyToOne(many2one, t, f);
					continue;
				}

				OneToMany one2many = f.getAnnotation(OneToMany.class);// 查找当前字段注解= OneToMany ?
				if (ObjectUtils.isNotEmpty(one2many)) {
					foreignKeyPropertyValue = getFieldValue(one2many.oneClassProperty(), t);
					handle_one2many(one2many, foreignKeyPropertyValue, t, f, selectFields);
					continue;
				}

				OneToOne one2one = f.getAnnotation(OneToOne.class);// 查找当前字段注解= OneToOne ?
				if (ObjectUtils.isNotEmpty(one2one)) {
					handle_One2One(one2one, foreignKeyPropertyValue, t, f);
					continue;
				}
			}
		}

		return list;

	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-1914:03:33
	 * @Desc:
	 *        <li>处理ManyToMany
	 */
	private void handle_Many2Many(ManyToMany many2many, String value, T t, Field f) {
		String foreignKey = getForeignKey(many2many, t);// 是否有指定的外键，如果没有则使用@TableId标定的字段值
		value = StringUtils.isNotBlank(foreignKey) ? foreignKey : value;

		try {
			Set<Object> sets = Sets.newHashSet();

			Type genericType = f.getGenericType();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];

				List<HashMap<String, Object>> result = baseMapper.many2manyQuery(new ManyToManyDto(many2many), value);
				for (HashMap<String, Object> map : result) {
					Object actualType = actualTypeArgument.getDeclaredConstructor().newInstance();
					map2BeanUtil(actualType, map, t);
					sets.add(actualType);
				}
				String methodForSet = "set" + StringUtils.capitalize(f.getName());// 向ManyToMany修饰的xxxSet集合赋值
				Method method = ReflectionUtils.findMethod(t.getClass(), methodForSet, Set.class);
				ReflectionUtils.invokeMethod(method, t, sets);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2011:07:03
	 * @Desc:
	 *        <li>处理OneToOne注解
	 */
	private void handle_One2One(OneToOne one2one, String value, T t, Field f) {

		try {

			HashMap<String, Object> result = baseMapper.one2oneQuery(one2one, value);

			Class<?> clazz = f.getType();
			Object actualType = clazz.getDeclaredConstructor().newInstance();
			map2BeanUtil(actualType, result, t);

			String setMethod = "set" + StringUtils.capitalize(f.getName());// 向OneToOne修饰的xxx字段赋值
			Method method = ReflectionUtils.findMethod(t.getClass(), setMethod, clazz);
			ReflectionUtils.invokeMethod(method, t, actualType);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:41:59
	 * @Desc:
	 *        <li>处理ManyToOne注解
	 */
	private void handle_ManyToOne(ManyToOne many2one, T t, Field f) {
		try {
			String value = BeanUtils.getProperty(t, many2one.joinField());
			HashMap<String, Object> result = baseMapper.many2oneQuery(many2one, value);
			if (MapUtils.isNotEmpty(result)) {
				Class<?> clazz = f.getType();
				Object actualType = clazz.getDeclaredConstructor().newInstance();
				map2BeanUtil(actualType, result, t);

				String setMethod = "set" + StringUtils.capitalize(f.getName());// 向OneToOne修饰的xxx字段赋值
				Method method = ReflectionUtils.findMethod(t.getClass(), setMethod, clazz);
				ReflectionUtils.invokeMethod(method, t, actualType);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2016:34:24
	 * @Desc:
	 *        <li>处理OneToMany 注解
	 *        <li>selectFields 格式如：a.order_sn,a.dealer_code,orderGoods.sourrce
	 *        <li>【a.】开头的是主表字段，形如【orderGoods.】开头的是子表字段，这里要剔除a.开头的主表字段；子表字段去掉【.】前面的字符串
	 */
	private void handle_one2many(OneToMany one2many, String value, T t, Field f, String selectFields) {

		try {
			List<Object> lists = Lists.newArrayList();
			Type genericType = f.getGenericType();

			getSelectFieldForChildTable(selectFields, one2many);

			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];

				List<HashMap<String, Object>> result = baseMapper.one2manyQuery(one2many, value);
				for (HashMap<String, Object> map : result) {
					Object actualType = actualTypeArgument.getDeclaredConstructor().newInstance();
					map2BeanUtil(actualType, map, t);
					lists.add(actualType);
				}

				String methodForSet = "set" + StringUtils.capitalize(f.getName());// 向OneToMany修饰的xxxSet集合赋值
				Method method = ReflectionUtils.findMethod(t.getClass(), methodForSet, List.class);
				ReflectionUtils.invokeMethod(method, t, lists);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-1910:44:25
	 * @Desc:
	 *        <li>获取带有注解：TableId字段的值 *
	 *        <li>当前方法 和 @getForeignKey(ManyToMany key, T clazz)互斥
	 */
	private String getTableIdPropertyValue(List<Field> fields, T clazz) {
		String value = null;
		try {
			for (Field f : fields) {
				TableId foreignKeyPropertyName = f.getAnnotation(TableId.class);
				if (ObjectUtils.isNotEmpty(foreignKeyPropertyName))
					value = BeanUtils.getProperty(clazz, f.getName());
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @author:weilai
	 * @Data:2020年11月20日 下午4:09:43
	 * @Desc:
	 *        <li>获取指定字段的值
	 */
	private String getFieldValue(String fieldName, T clazz) {
		String value = null;
		try {
			value = BeanUtils.getProperty(clazz, fieldName);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-1911:02:06
	 * @Desc:
	 *        <li>获取ManyToMany 中foreignKey 指定的字段值
	 *        <li>当前方法 和 @getTableIdPropertyValue(Field f, T clazz)互斥
	 */
	private String getForeignKey(ManyToMany key, T clazz) {
		String value = null;
		try {
			String foreignKey = key.foreignKey();// 有值则根据此值去查询多对多关联表
			if (StringUtils.isNotBlank(foreignKey))
				value = BeanUtils.getProperty(clazz, foreignKey);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-1914:32:15
	 * @Desc:
	 *        <li>因为查询数据库返回的结果是Map类型, 此Map的键值是数据表字段名称，
	 *        <li>而实体bean中字段可能 用TableField修饰后和数据表字段 有可能，有可能不对应，所以没法
	 *        <li>使用BeanUtils.populate(bean, Map),只能自己写方法
	 */
	private Object map2BeanUtil(Object obj, HashMap<String, Object> map, T t) {
		if (MapUtils.isNotEmpty(map)) {
			Object value = null;
			Map<String, Method> methodMap = Maps.newHashMap();
			Method[] methods = obj.getClass().getMethods();

			for (Method m : methods)
				methodMap.put(m.getName(), m);

			Field[] fields = FieldUtils.getAllFields(obj.getClass());

			for (Field fd : fields) {// obj是javaBean,遍历其属性
				TableId td = fd.getAnnotation(TableId.class);
				TableField tf = fd.getAnnotation(TableField.class);

				if (ObjectUtils.isNotEmpty(tf))
					value = map.get(tf.value());// 有TableField修饰
				else if (ObjectUtils.isNotEmpty(td))
					value = map.get(td.value());// 有TableId修饰
				else
					value = map.get(Hump2underline.build(fd.getName()));// 没有TableField修饰，则采用驼峰命名

				Method method = methodMap.get("set" + StringUtils.capitalize(fd.getName()));

				if (ObjectUtils.isNotEmpty(value) && ObjectUtils.isNotEmpty(method)) {
					try {
						ReflectionUtils.invokeMethod(method, obj, value);// XXXSet<对象赋值>
					} catch (Exception e) {
						System.err.println(method + "," + value.getClass());
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 
	 * @author: weilai
	 * @Data:2021年1月30日下午3:15:20
	 * @Desc:
	 *        <li>很好的方法：获取类所有属性，包括父类，爷爷类
	 */
	private static List<Field> getAllField(Object model) {
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
	 * 
	 * @author: weilai
	 * @Data:2021年1月30日下午3:17:11
	 * @Desc:
	 *        <li>str格式如：a.order_sn,a.dealer_code,items.source,items.sn;
	 *        <li>【a.】开头的是主表字段，形如【items.】开头的是子表字段，这里要剔除a.开头的主表字段；子表字段去掉【.】前面的字符串
	 *        <li>结果返回：source,sn 并设置到 OneToMany的selectColumn属性中去
	 */
	private OneToMany getSelectFieldForChildTable(String str, OneToMany one2many) {

		try {
			List<String> result = Lists.newArrayList();

			if (StringUtils.isNotBlank(str)) {
				String[] arry = StringUtils.split(str, ",");
				for (String s : arry) {
					if (!StringUtils.startsWith(s, "a."))
						result.add(StringUtils.substringAfterLast(s, "."));
				}
			}

			String sl = result.size() > 0 ? StringUtils.join(result, ",") : "*";

			InvocationHandler h = Proxy.getInvocationHandler(one2many);
			Field hField = h.getClass().getDeclaredField("memberValues");
			hField.setAccessible(true);
			Map<String, String> memberValues = (Map<String, String>) hField.get(h);
			if (!selectField.containsKey(one2many.joinColumn()) && StringUtils.isNotBlank(one2many.selectColumn()))
				selectField.put(one2many.joinColumn(), one2many.selectColumn());

			String defalut = selectField.get(one2many.joinColumn());
			if (StringUtils.equals(sl, "*") && StringUtils.isNotBlank(defalut))
				memberValues.put("selectColumn", defalut);
			else
				memberValues.put("selectColumn", sl);

		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		return one2many;
	}
}
