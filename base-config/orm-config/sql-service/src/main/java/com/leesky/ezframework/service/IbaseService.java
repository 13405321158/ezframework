/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leesky.ezframework.query.QueryFilter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


public interface IbaseService<T> extends IService<T> {

	/**
	 * 插入一条记录（选择字段，策略插入）
	 */
	T insert(T entity);


	
	/**
	 * 插入（批量）
	 *
	 * @param entityList 实体对象集合
	 */

	void insertBatch(Collection<T> entityList);

	/**
	 * 根据 ID 删除
	 *
	 * @param id 主键ID
	 */
	void delById(Serializable id);

	/**
	 * 根据 entity 条件，删除记录
	 *
	 * @param queryWrapper 实体包装类
	 *                     {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	void del(Wrapper<T> queryWrapper);

	/**
	 * 删除（根据ID 批量删除）
	 *
	 * @param idList 主键ID列表
	 */
	void delByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据 ID 选择修改
	 *
	 * @param entity 实体对象
	 */
	Integer update(T entity);

	/**
	 * 批量更新
	 */
	boolean updateBatch(Collection<T> entityList);

	/**
	 * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
	 *
	 * @param updateWrapper 实体对象封装操作类
	 *                      {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
	 */
	void edit(T entity, Wrapper<T> updateWrapper);

	/**
	 * 根据 ID 查询
	 *
	 * @param id 主键ID
	 */
	T getById(Serializable id, Boolean lazy);

	/**
	 * 查询（根据ID 批量查询）
	 *
	 * @param idList 主键ID列表
	 */
	List<T> listByIds(Collection<? extends Serializable> idList, Boolean lazy);

	/**
	 * 根据 Wrapper，查询一条记录 <br/>
	 * <p>
	 * 结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
	 * </p>
	 */
	T getOne(String id);

	/**
	 * 根据 Wrapper，查询一条记录 <br/>
	 * <p>
	 * 结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
	 * </p>
	 */
	T getModel(String id, Boolean lazy);

	/**
	 * 根据 Wrapper，查询一条记录 <br/>
	 * <p>
	 * 结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
	 * </p>
	 *
	 * @param queryWrapper 实体对象封装操作类
	 *                     {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	T getModel(Wrapper<T> queryWrapper, Boolean lazy);

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月18日上午8:35:55
	 * @Desc:
	 *
	 */

	List<T> list(Wrapper<T> queryWrapper, Boolean lazy);

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月18日上午8:35:55
	 * @Desc:
	 *
	 */

	List<T> list(QueryFilter<T> filter, Boolean lazy);

	/**
	 * 
	 * @author: weilai
	 * @Data:2021年1月8日上午9:34:07
	 * @Desc:
	 *        <li>查询条件含有子表字段
	 */
	public List<T> list(QueryFilter<T> filter, Boolean lazy, Boolean queryIncludeChildren);

	/**
	 * 查询所有
	 *
	 * @see Wrappers#emptyWrapper()
	 */
	List<T> list(Boolean lazy);

	/**
	 * 翻页查询
	 */
	Page<T> page(QueryFilter<T> filter, Boolean lazy);


	
	/**
	 * 翻页查询: 查询条件含有子表字段
	 */
	Page<T> page(QueryFilter<T> filter, Boolean lazy, Boolean queryIncludeChildren);

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月20日上午10:08:29
	 * @Desc: 查询全部数据个数
	 *
	 */
	int count();

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年2月20日上午10:08:57
	 * @Desc: 根据条件查询记录个数
	 *
	 */
	int count(QueryWrapper<T> queryWrapper);

}
