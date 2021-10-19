package com.leesky.ezframework.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.mybatis.save.SaveHandler;
import com.leesky.ezframework.mybatis.service.IeeskyService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("unchecked")
public class LeeskyServiceImpl<M extends IeeskyMapper<T>, T> implements IeeskyService<T> {

    @Autowired
    protected M baseMapper;

    @Autowired
    protected AutoMapper autoMapper;

    @Autowired
    protected SaveHandler<T> saveHandler;

    protected Class<T> entityClass = currentModelClass();

    protected Class<M> mapperClass = currentMapperClass();

    /**
     * 描述: 根据记录主键查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    @Transactional(readOnly = true)
    public T findOne(Serializable id) {
        return this.baseMapper.selectById(id);

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
        return this.baseMapper.selectOne(filter);

    }

    /**
     * 描述: 查询全部
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    public List<T> findAll() {
        return this.baseMapper.selectList(Wrappers.emptyWrapper());

    }

    /**
     * 描述: 根据主键集合查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    public List<T> findAll(Collection<? extends Serializable> idList) {
        return this.baseMapper.selectBatchIds(idList);
    }

    /**
     * 描述:根据wrapper过滤器 查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    @Override
    public List<T> findAll(QueryFilter<T> filter) {
        return this.baseMapper.selectList(filter);
    }

    /**
     * 描述:根据wrapper过滤器 分页查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    @Override
    public Page<T> page(QueryFilter<T> filter) {

        Integer pageSize = filter.getParam().getLimit();
        Integer pageoffset = filter.getParam().getPage();
        Page<T> page = new Page<>(pageoffset, pageSize);

        return this.baseMapper.selectPage(page, filter);
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
            this.autoMapper.insert(entity, this.baseMapper, saveHandler);
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
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(T entity) {
        this.baseMapper.updateById(entity);
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(UpdateWrapper<T> filter) {
        this.baseMapper.update(null, filter);
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(Collection<T> entityList) {
        this.baseMapper.updateBatch(entityList);
    }

    /**
     * <li>: 根据id删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    @Override
    public void delete(Serializable id) {
        this.baseMapper.deleteById(id);
    }

    /**
     * <li>:根据条件删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    @Override
    public void delete(QueryFilter<T> filter) {
        this.baseMapper.delete(filter);
    }

    /**
     * <li>: 批量删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    @Override
    public void delete(Collection<? extends Serializable> idList) {
        this.baseMapper.deleteBatchIds(idList);
    }


    private Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 0);
    }

    private Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 1);
    }
}