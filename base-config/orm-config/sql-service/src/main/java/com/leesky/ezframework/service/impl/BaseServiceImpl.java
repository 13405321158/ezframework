package com.leesky.ezframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.join.interfaces.one2many.OneToMany;
import com.leesky.ezframework.join.mapper.AutoMapper;
import com.leesky.ezframework.join.mapper.LeeskyMapper;
import com.leesky.ezframework.join.query.JoinQuery;
import com.leesky.ezframework.model.SuperModel;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.service.IbaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: weilai
 * @Data:下午7:19:05,2020年1月27日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc:
 *        <li>
 */
@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseServiceImpl<M extends LeeskyMapper<T>, T> extends ServiceImpl<LeeskyMapper<T>, T>
		implements IbaseService<T> {

	protected Class<T> entityClass = currentModelClass();
	int DEFAULT_BATCH_SIZE = 1000;// 默认批次提交数量
	@Autowired
	private M repo;
	@Autowired
	private JoinQuery<T, M> joinQuery;
	@Autowired
	private AutoMapper<T, M> autoMapper;

	/**
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public T insert(T entity) {

		if (entity instanceof SuperModel) {

			SuperModel e = ((SuperModel) entity);
			Date date = new Date();
			e.setCreateDate(date);
			e.setModifyDate(date);

		}

		this.repo.insert(entity);

		handleOne2Many(entity);

		return entity;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @return
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBatch(Collection<T> entityList) {

		long start = System.currentTimeMillis();

		try (SqlSession session = SqlHelper.FACTORY.openSession(ExecutorType.BATCH)) {

			entityList.stream().forEach(e -> insert(e));

			session.commit();
			session.clearCache();

		}

		long end = System.currentTimeMillis();
		log.info("本次批量插入[{}]条记录,耗时{}毫秒", entityList.size(), String.valueOf(end - start));

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delById(Serializable id) {

		this.repo.deleteById(id);

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void del(Wrapper<T> queryWrapper) {

		this.repo.delete(queryWrapper);

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delByIds(Collection<? extends Serializable> idList) {

		this.repo.deleteBatchIds(idList);

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer update(T entity) {

		if (entity instanceof SuperModel) {

			SuperModel e = ((SuperModel) entity);
			e.setModifyDate(new Date());

		}

		return this.repo.updateById(entity);

	}

	/**
	 * 批量更新
	 */

	@Override
	public boolean updateBatch(Collection<T> entityList) {

		for (T t : entityList) {

			if (t instanceof SuperModel) {

				SuperModel e = ((SuperModel) t);
				e.setModifyDate(new Date());

			}

		}

		return super.updateBatchById(entityList);

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void edit(T entity, Wrapper<T> updateWrapper) {

		if (entity instanceof SuperModel) {

			SuperModel e = ((SuperModel) entity);
			e.setModifyDate(new Date());

		}

		this.repo.update(entity, updateWrapper);

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public T getById(Serializable id, Boolean lazy) {

		T ret = this.repo.selectById(id);
		if (ObjectUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(Lists.newArrayList(ret), repo, null);

		return ret;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public List<T> listByIds(Collection<? extends Serializable> idList, Boolean lazy) {

		List<T> ret = this.repo.selectBatchIds(idList);
		if (CollectionUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(Lists.newArrayList(ret), repo, null);

		return ret;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午7:37:18,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public T getModel(Wrapper<T> queryWrapper, Boolean lazy) {

		T ret = this.repo.selectOne(queryWrapper);
		if (ObjectUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(Lists.newArrayList(ret), repo, null);

		return ret;

	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public T getModel(String id, Boolean lazy) {

		T ret = this.repo.selectById(id);
		if (ObjectUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(Lists.newArrayList(ret), repo, null);

		return ret;

	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public T getOne(String id) {

		T ret = this.repo.selectById(id);

		return ret;

	}

	/**
	 * @desc 根据条件查询，返回list
	 */
	@Override
	@Transactional
	public List<T> list(Wrapper<T> queryWrapper, Boolean lazy) {

		List<T> ret = this.repo.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(ret, repo, null);

		return ret;

	}

	/**
	 * @desc 根据条件查询，返回list
	 */
	@Override
	@Transactional
	public List<T> list(QueryFilter<T> filter, Boolean lazy) {

		List<T> ret = this.repo.selectList(filter);
		if (CollectionUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(ret, repo, filter.getParam().getExt());
		return ret;

	}

	/**
	 * @desc 查询条件带有子表字段，返回list
	 */
	@Override
	@Transactional
	public List<T> list(QueryFilter<T> filter, Boolean lazy, Boolean queryIncludeChildren) {

		List<T> ret = Lists.newArrayList();
		// 获取查询条件
		String queryStr = filter.getParam().getQueryStr();

		if (queryIncludeChildren && StringUtils.isNotBlank(queryStr))
			ret = this.joinQuery.findAll(filter, entityClass, repo);
		else
			ret = this.repo.selectList(filter);

		if (CollectionUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(ret, repo, filter.getParam().getExt());

		return ret;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午8:34:37,2020年1月27日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public List<T> list(Boolean lazy) {

		List<T> ret = this.repo.selectList(Wrappers.emptyWrapper());
		if (CollectionUtils.isNotEmpty(ret) && lazy)
			this.autoMapper.mapper(ret, repo, null);

		return ret;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午5:22:53,2020年1月29日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public Page<T> page(QueryFilter<T> filter, Boolean lazy) {

		Integer pageSize = filter.getParam().getLimit();
		Integer pageoffset = filter.getParam().getPage();
		Page<T> page = new Page<>(pageoffset, pageSize);

		Page<T> ret = this.repo.selectPage(page, filter);

		if (CollectionUtils.isNotEmpty(ret.getRecords()) && lazy)
			this.autoMapper.mapper(ret.getRecords(), repo, null);
		return ret;

	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 下午5:22:53,2020年1月29日
	 * @desc:
	 *        <li>描述：
	 */
	@Override
	@Transactional
	public Page<T> page(QueryFilter<T> filter, Boolean lazy, Boolean queryIncludeChildren) {
		Page<T> ret = null;
		Integer pageSize = filter.getParam().getLimit();
		Integer pageoffset = filter.getParam().getPage();
		Page<T> page = new Page<>(pageoffset, pageSize);

		// 获取查询条件
		String queryStr = filter.getParam().getQueryStr();
		if (queryIncludeChildren && StringUtils.isNotBlank(queryStr))
			ret = this.joinQuery.pageQuery(filter, page, entityClass, repo);
		else
			ret = this.repo.selectPage(page, filter);

		if (CollectionUtils.isNotEmpty(ret.getRecords()) && lazy)
			this.autoMapper.mapper(ret.getRecords(), repo, filter.getParam().getExt());

		return ret;

	}

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月20日上午10:08:29
	 * @Desc: 查询全部数据个数
	 *
	 */
	@Override
	@Transactional
	public int count() {

		return this.repo.selectCount(Wrappers.emptyWrapper());

	}

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月20日上午10:08:57
	 * @Desc: 根据条件查询记录个数
	 *
	 */
	@Override
	@Transactional
	public int count(QueryWrapper<T> queryWrapper) {

		return this.repo.selectCount(queryWrapper);

	}

//-----------使用此方法前提必须是驼峰命名法-------私有方法------------------------------------------
	private void handleOne2Many(T entity) {

		List<Field> fields = FieldUtils.getFieldsListWithAnnotation(entityClass, OneToMany.class);

		if (CollectionUtils.isNotEmpty(fields)) {

			try {

				// One方类中 可能有多个 OneToMany注解
				for (Field field : fields) {

					OneToMany s = field.getAnnotation(OneToMany.class);

					String joinColumName = s.joinField();
					Field jField = ReflectionUtils.findField(entityClass, joinColumName);

					List list = (List) getValue(field, entity);// 获取Many方(子表)的实体,并赋值
					if (CollectionUtils.isNotEmpty(list))
						list.stream().forEach(e -> setValue(e, getValue(jField, entity), joinColumName));// 循环赋值

				}

			} catch (IllegalArgumentException | SecurityException e) {

				e.printStackTrace();

			}

		}

	}

	private Object getValue(Field key, T entity) {

		String methodForGet = "get" + StringUtils.capitalize(key.getName());
		Method method = ReflectionUtils.findMethod(entityClass, methodForGet);
		return ReflectionUtils.invokeMethod(method, entity);

	}

	private void setValue(Object obj, Object id, String joinField) {

		Field manyId = FieldUtils.getDeclaredField(obj.getClass(), joinField, true);
		String methodForSet = "set" + StringUtils.capitalize(manyId.getName());
		Class<?> decClass = manyId.getType();
		Method method = ReflectionUtils.findMethod(obj.getClass(), methodForSet, decClass);

		if (ObjectUtils.isNotEmpty(method)) {

			ReflectionUtils.invokeMethod(method, obj, id);

		}

	}

}
