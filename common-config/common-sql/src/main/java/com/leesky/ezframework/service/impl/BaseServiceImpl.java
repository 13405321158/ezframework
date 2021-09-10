/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.SaveWithRelation;
import com.leesky.ezframework.model.SuperModel;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.query.QueryWithRelation;
import com.leesky.ezframework.service.IbaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseServiceImpl<M extends IbaseMapper<T>, T> extends ServiceImpl<IbaseMapper<T>, T> implements IbaseService<T> {

    @Autowired
    private SaveWithRelation<T> saveWithRelation;

    @Autowired
    private QueryWithRelation<T> queryWithRelation;

    @Override
    public T findOne(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public T findOne(QueryFilter<T> filter) {
        return this.baseMapper.selectOne(filter);
    }

    @Override
    public List<T> findAll(QueryFilter<T> filter) {
        return this.baseMapper.selectList(filter);
    }
    @Override
    public List<T> findAll() {
        return this.baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<T> findAll(Map<String, String> param) {
        List<T> data = this.baseMapper.selectList(Wrappers.emptyWrapper());
        if (MapUtils.isNotEmpty(param))
            data.forEach(e -> this.queryWithRelation.relationship(e, param,this.baseMapper));
        return data;
    }
    @Override
    public Page<T> findByPage(QueryFilter<T> filter) {

        Integer payoffs = filter.getParam().getPage();
        Integer pageSize = filter.getParam().getLimit();
        Page<T> page = new Page<>(payoffs, pageSize);

        return this.baseMapper.selectPage(page, filter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(T entity, Boolean relation) {
        if (relation)
            saveWithRelation.relationship(entity, this.baseMapper);
        else
            this.baseMapper.insert(entity);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Collection<T> entityList) {
        long start = System.currentTimeMillis();
        this.baseMapper.insertBatch(entityList);
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
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    public long count() {
        return this.baseMapper.selectCount(Wrappers.emptyWrapper());
    }


    @Override
    public long count(QueryWrapper<T> filter) {
        return this.baseMapper.selectCount(filter);
    }

}
