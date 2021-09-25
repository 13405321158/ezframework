package com.leesky.ezframework.mybatis.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface IbaseService<T> {

	/**
	 * <li>根据记录主键查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	T findOne(Serializable id);

	/**
	 * <li>自定义查询条件，返回一条记录
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	T findOne(Wrapper<T> filter);

	/**
	 * <li>查询全部
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	List<T> findAll();

	/**
	 * <li>根据主键集合查询
	 *
	 * @作者: 魏来
	 * @日期: 2021/8/21 下午12:39
	 **/
	List<T> findAll(Collection<? extends Serializable> idList);

	/**
	 * 描述：根据字段集合(map)查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:00:26
	 */
	List<T> findAll(Map<String, Object> columnMap);

	/**
	 * <li>根据wrapper过滤器 查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:15:49
	 */
	List<T> findAll(Wrapper<T> filter);

	/**
	 * <li>根据wrapper过滤器 分页查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:20:12
	 */
	<E extends IPage<T>> E findByPage(E page, Wrapper<T> filter);

	/**
	 * <li>无条件 分页查询
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:20:12
	 */
	<E extends IPage<T>> E findByPage(E page);

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是单体 Bean
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	void initializeEntity(T t, String... propertyNames);

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是set集合
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	void initializeSet(Set<T> list, String... propertyNames);

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是list集合
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	void initializeList(List<T> list, String... propertyNames);

	/**
	 * <li>根据 propertyNames 自动加载映射关系</li>；
	 * <li>适用结果集是object[可以是list或set或page或单体bean]</li>
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	<E extends IPage<T>> void initialize(Object t, String... propertyNames);

	/**
	 * <li>根据 propertyNames 自动加载映射关系；适用结果集是page
	 * 
	 * @作者: 魏来
	 * @日期: 2021年9月25日 上午8:43:04
	 */
	<E extends IPage<T>> void initializePage(E page, String... propertyNames);

}