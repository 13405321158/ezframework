/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.MappingUtils;
import com.leesky.ezframework.model.SuperModel;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.service.IbaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseServiceImpl<M extends IbaseMapper<T>, T> extends ServiceImpl<IbaseMapper<T>, T> implements IbaseService<T> {

	int DEFAULT_BATCH_SIZE = 1000;// 默认批次提交数量

	@Autowired
	private MappingUtils<T> mappingUtils;

	@Override
	public T getOne(String id) {
		return this.baseMapper.selectById(id);
	}

	@Override
	public T getOne(QueryFilter<T> filter) {
		return this.baseMapper.selectOne(filter);
	}

	@Override
	public List<T> list(QueryFilter<T> filter) {
		return this.baseMapper.selectList(filter);
	}

	@Override
	public Page<T> page(QueryFilter<T> filter) {

		Integer payoffs = filter.getParam().getPage();
		Integer pageSize = filter.getParam().getLimit();
		Page<T> page = new Page<>(payoffs, pageSize);

		return this.baseMapper.selectPage(page, filter);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(T entity, Boolean relation) {
		if (relation)
			mappingUtils.relationship(entity, this.baseMapper);
		else
			this.baseMapper.insert(entity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(Collection<T> entityList) {

		long start = System.currentTimeMillis();
		this.saveBatch(entityList, this.DEFAULT_BATCH_SIZE);
		long end = System.currentTimeMillis();
		log.info("本次批量插入[{}]条记录,耗时{}毫秒", entityList.size(), (end - start));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void del(Serializable id) {
		this.baseMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void del(QueryFilter<T> filter) {
		this.baseMapper.delete(filter);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void del(Collection<? extends Serializable> ids) {
		this.baseMapper.deleteBatchIds(ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void edit(T entity) {

		if (entity instanceof SuperModel) {
			SuperModel e = ((SuperModel) entity);
			e.setModifyDate(new Date());
		}

		this.baseMapper.updateById(entity);
	}

	@Override
	public void edit(Collection<T> entityList) {

		for (T t : entityList) {
			if (t instanceof SuperModel) {
				SuperModel e = ((SuperModel) t);
				e.setModifyDate(new Date());
			}
		}
		this.updateBatchById(entityList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void edit(T entity, Wrapper<T> updateWrapper) {
		if (entity instanceof SuperModel) {
			SuperModel e = ((SuperModel) entity);
			e.setModifyDate(new Date());
		}
		this.baseMapper.update(entity, updateWrapper);
	}

	@Override
	public int count() {
		return this.baseMapper.selectCount(Wrappers.emptyWrapper());
	}

	@Override
	public int count(QueryWrapper<T> filter) {
		return this.baseMapper.selectCount(filter);
	}

}
