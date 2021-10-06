package com.leesky.ezframework.mybatis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leesky.ezframework.mybatis.annotation.DisableAutoMapper;
import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.mybatis.mapper.IbaseMapper;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.mybatis.save.SaveHandler;
import com.leesky.ezframework.mybatis.service.IbaseService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class BaseServiceImpl<M extends IbaseMapper<T>, T> extends ServiceImpl<IbaseMapper<T>, T> implements IbaseService<T> {

	@Autowired
	protected AutoMapper autoMapper;

	@Autowired
	private SaveHandler<T> saveHandler;

	protected boolean autoMapperEnabled = true;

	public BaseServiceImpl() {
		Class<?> clazz = this.getClass();
		if (clazz.getAnnotation(DisableAutoMapper.class) != null && clazz.getAnnotation(DisableAutoMapper.class).value() == true)
			autoMapperEnabled = false;

	}

	/**
	 * 描述: 根据记录主键查询
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	@Override
	@Transactional(readOnly = true)
	public T findOne(Serializable id) {
		T data = this.baseMapper.selectById(id);
		return isAutoMapperEnabled() ? this.autoMapper.mapperEntity(data) : data;
	}

	/**
	 * 描述: 自定义查询条件，返回一条记录
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	@Override
	@Transactional(readOnly = true)
	public T findOne(QueryFilter<T> filter) {
		T data = this.baseMapper.selectOne(filter);

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntity(data) : data;
	}

	/**
	 * 描述: 查询全部
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	@Override
	public List<T> findAll() {
		List<T> data = this.baseMapper.selectList(Wrappers.emptyWrapper());

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityList(data) : data;

	}

	/**
	 * 描述: 根据主键集合查询
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	@Override
	public List<T> findAll(Collection<? extends Serializable> idList) {
		List<T> data = this.baseMapper.selectBatchIds(idList);

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityList(data) : data;

	}

	/**
	 * 描述：根据字段集合(map)查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:00:26
	 */
	@Override
	public List<T> findAll(Map<String, Object> columnMap) {
		List<T> data = this.baseMapper.selectByMap(columnMap);

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityList(data) : data;

	}

	/**
	 * 描述:根据wrapper过滤器 查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:15:49
	 */
	@Override
	public List<T> findAll(QueryFilter<T> filter) {
		List<T> data = this.baseMapper.selectList(filter);

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityList(data) : data;

	}

	/**
	 * 描述:根据wrapper过滤器 分页查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:20:12
	 */
	@Override
	public <E extends IPage<T>> E findByPage(E page, QueryFilter<T> filter) {
		E data = this.baseMapper.selectPage(page, filter);

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityPage(data) : data;
	}

	/**
	 * 描述:无条件 分页查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:20:12
	 */
	@Override
	public <E extends IPage<T>> E findByPage(E page) {
		E data = this.baseMapper.selectPage(page, Wrappers.emptyWrapper());

		return isAutoMapperEnabled() ? this.autoMapper.mapperEntityPage(data) : data;

	}

	/**
	 * <li>relation=false 不处理聚合关系</li>
	 * <li>relation=true 则同时存储one2one、many2many，one2Many，many2one 关系</li>
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午4:48
	 **/
	@Override
	public void insert(T entity, Boolean withRelation) {
		if (withRelation)
			this.autoMapper.insert(entity, this.baseMapper,saveHandler);
		else
			this.baseMapper.insert(entity);
	}

	/**
	 * 描述: 批量插入数据
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:51
	 **/
	@Override
	public void insert(List<T> entityList, Boolean withRelation) {

		if (withRelation)
			this.autoMapper.insert(entityList, this.baseMapper, saveHandler);
		else
			this.baseMapper.insertBatch(entityList);

	}

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是单体 Bean
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	@Override
	@Transactional(readOnly = true)
	public void initializeEntity(T t, String... propertyNames) {
		for (String propertyName : propertyNames)
			autoMapper.mapperEntity(t, propertyName);

	}

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是list集合
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	@Override
	@Transactional(readOnly = true)
	public void initializeList(List<T> list, String... propertyNames) {
		for (String propertyName : propertyNames)
			autoMapper.mapperEntityList(list, propertyName);

	}

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是set集合
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	@Override
	@Transactional(readOnly = true)
	public void initializeSet(Set<T> set, String... propertyNames) {
		for (String propertyName : propertyNames)
			autoMapper.mapperEntitySet(set, propertyName);

	}

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是page
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	@Override
	@Transactional(readOnly = true)
	public <E extends IPage<T>> void initializePage(E page, String... propertyNames) {
		for (String propertyName : propertyNames)
			autoMapper.mapperEntityPage(page, propertyName);

	}

	/**
	 * <li>根据 propertyNames 自动加载映射关系</li>；
	 * <li>适用结果集是object[可以是list或set或page或单体bean]</li>
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <E extends IPage<T>> void initialize(Object object, String... propertyNames) {
		if (object != null) {
			if (object.getClass() == List.class)
				initializeList((List<T>) object, propertyNames);
			else if (object.getClass() == Set.class)
				initializeSet((Set<T>) object, propertyNames);
			else if (object instanceof IPage)
				initializePage((E) object, propertyNames);
			else
				initializeEntity((T) object, propertyNames);

		}
	}

}