/*
 * @作者: 魏来
 * @日期: 2021/10/8  下午3:49
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.condition.TableIdCondition;
import com.leesky.ezframework.mybatis.mapper.IbaseMapper;
import com.leesky.ezframework.mybatis.utils.JoinUtil;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <li>描述:
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class One2manyHandler<T> {

	private final ObjectFactory<SqlSession> factory;


	public void handler(String[] fields, T entity, IbaseMapper ibaseMapper) throws Exception {

		// 1、插入entity实体
		TableIdCondition tf = new TableIdCondition(entity.getClass());
		String entityKey = BeanUtils.getProperty(entity, tf.getFieldOfTableId().getName());// o2m对象的主键值
		if (StringUtils.isBlank(entityKey)) {
			ibaseMapper.insert(entity);
			entityKey = BeanUtils.getProperty(entity, tf.getFieldOfTableId().getName());// o2m对象的主键值
		}

		for (String f : fields) {
			List<Object> noKey02 = Lists.newArrayList();
			List<Object> noKey01 = Lists.newArrayList();
			Map<Object, Object> haveKey = Maps.newHashMap();

			Field field = entity.getClass().getDeclaredField(f);
			field.setAccessible(true);
			Object m2m = field.get(entity);// obj 就是要存储的实体集合（entity中 的 one2many关系实体），一般是set或list集合
			List list = m2m instanceof Set ? Lists.newArrayList((Set) m2m) : (List) m2m;
			// 2、查询集合中的实体是否在数据库中存在： 不存在的存储到数据表
			FieldCondition<T> fc = new FieldCondition<>(entity, field, false, factory);
			String keyColumn = fc.getTableId().value();

			if (CollectionUtils.isNotEmpty(list)) {

				for (Object e : list) {
					String v = BeanUtils.getProperty(e, fc.getFieldOfTableId().getName());// m2m对象的主键值
					if (StringUtils.isBlank(v))
						noKey01.add(e);
					else
						haveKey.put(v, e);
				}

				Class<?> m = list.get(0).getClass();
				IbaseMapper iMapper = (IbaseMapper) SpringContextHolder.getBean(JoinUtil.buildMapperBeanName(m.getName()));

				if (MapUtils.isNotEmpty(haveKey)) {
					QueryWrapper filter = new QueryWrapper().select(keyColumn);
					filter.in(keyColumn, haveKey.keySet());
					List<Map> data = iMapper.selectMaps(filter);
					data.forEach(e -> noKey02.add(e.get(keyColumn)));
					haveKey.keySet().removeIf(noKey02::contains);
				}

				Stream<Object> stream = Stream.concat(noKey01.stream(), haveKey.values().stream());
				iMapper.insertBatch(stream.collect(Collectors.toList()));
			}

			// 3、一竿子打死方式：把one方主键插入到 many方
			if (CollectionUtils.isNotEmpty(list)) {
				List keys = Lists.newArrayList();
				Class<?> manyMapper = fc.getEntityMapper().targetMapper();
				IbaseMapper m2mMapper = (IbaseMapper) factory.getObject().getMapper(manyMapper);
				for (Object e : list) {// 3.1 赋值
					BeanUtils.setProperty(e, Hump2underline.lineToHump(fc.getJoinColumn().referencedColumnName()), entityKey);
					keys.add(BeanUtils.getProperty(e, fc.getFieldOfTableId().getName()));
				}
				UpdateWrapper uw = new UpdateWrapper();
				uw.set(fc.getJoinColumn().referencedColumnName(), entityKey);
				uw.in(keyColumn, keys);
				m2mMapper.update(null, uw);
			}

		}
	}

}
