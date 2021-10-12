/*
 * @作者: 魏来
 * @日期: 2021/10/8  下午3:50
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.condition.TableIdCondition;
import com.leesky.ezframework.mybatis.mapper.IleeskyMapper;

import lombok.RequiredArgsConstructor;

/**
 * <li>描述:
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Many2oneHandler<T> {

	private final ObjectFactory<SqlSession> factory;

	public void handler(String[] fields, T entity, IleeskyMapper ibaseMapper) throws Exception {

		// 1、插入entity实体
		TableIdCondition tf = new TableIdCondition(entity.getClass());
		String entityKey = BeanUtils.getProperty(entity, tf.getFieldOfTableId().getName());// m2o对象的主键值
		if (StringUtils.isBlank(entityKey)) {
			ibaseMapper.insert(entity);
			entityKey = BeanUtils.getProperty(entity, tf.getFieldOfTableId().getName());// m2o对象的主键值
		}

		for (String f : fields) {

			Field field = entity.getClass().getDeclaredField(f);
			field.setAccessible(true);

			FieldCondition<T> fc = new FieldCondition<>(entity, field, false, factory);

			// 2、查询数据表中是否存在 
			String v = BeanUtils.getProperty(field.get(entity), fc.getFieldOfTableId().getName());// m2o对象的主键值
			IleeskyMapper m2oMapper = (IleeskyMapper) factory.getObject().getMapper(fc.getEntityMapper().targetMapper());
			if (StringUtils.isNotBlank(v)) {

				QueryWrapper filter = new QueryWrapper<>();
				filter.eq(fc.getFieldOfTableId().getName(), v);
				filter.select(fc.getFieldOfTableId().getName());
				Object data = m2oMapper.selectOne(filter);

				if (ObjectUtils.isEmpty(data))
					m2oMapper.insert(field.get(entity));
				else
					update(fc.getJoinColumn().name(), tf.getFieldOfTableId().getName(), v, entityKey, ibaseMapper);

			} else {
				m2oMapper.insert(field.get(entity));
				v = BeanUtils.getProperty(field.get(entity), fc.getFieldOfTableId().getName());// m2o对象的主键值
				update(fc.getJoinColumn().name(), tf.getFieldOfTableId().getName(), v, entityKey, ibaseMapper);
			}
		}
	}

	private void update(String fc, String tf, String v, String entityKey, IleeskyMapper ibaseMapper) {
		UpdateWrapper uw = new UpdateWrapper();
		uw.set(fc, v);
		uw.eq(tf, entityKey);
		ibaseMapper.update(null, uw);
	}
}
