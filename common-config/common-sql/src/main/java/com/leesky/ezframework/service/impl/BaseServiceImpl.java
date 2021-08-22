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
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.leesky.ezframework.join.mapper.AutoMapper;
import com.leesky.ezframework.join.mapper.LeeskyMapper;
import com.leesky.ezframework.join.query.JoinQuery;
import com.leesky.ezframework.model.BaseAutoModel;
import com.leesky.ezframework.model.BaseUuidModel;
import com.leesky.ezframework.model.SuperModel;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.service.IbaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseServiceImpl<M extends LeeskyMapper<T>, T> extends ServiceImpl<LeeskyMapper<T>, T> implements IbaseService<T> {
    @Autowired
    private M repo;
    @Autowired
    private JoinQuery<T, M> joinQuery;
    @Autowired
    private AutoMapper<T, M> autoMapper;

    int DEFAULT_BATCH_SIZE = 1000;// 默认批次提交数量
    protected Class<T> entityClass = currentModelClass();

    @Override
    @Transactional
    public T getOne(String id) {
        return this.repo.selectById(id);
    }

    @Override
    @Transactional
    public T getOne(QueryFilter<T> filter) {
        return this.repo.selectOne(filter);
    }

    @Override
    @Transactional
    public List<T> list(QueryFilter<T> filter) {
        return this.repo.selectList(filter);
    }


    @Override
    @Transactional
    public Page<T> page(QueryFilter<T> filter) {

        Integer payoffs = filter.getParam().getPage();
        Integer pageSize = filter.getParam().getLimit();
        Page<T> page = new Page<>(payoffs, pageSize);

        return this.repo.selectPage(page, filter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(T entity) {
        if (entity instanceof SuperModel) {
            SuperModel e = ((SuperModel) entity);
            Date date = new Date();
            e.setCreateDate(date);
            e.setModifyDate(date);
        }
        this.repo.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(T entity, Boolean relation) {
        String id = null;
        this.insert(entity);
        if (entity instanceof BaseUuidModel)
            id = ((BaseUuidModel) entity).getId();
        if (entity instanceof BaseAutoModel)
            id = ((BaseAutoModel) entity).getId();

        System.out.println(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(Collection<T> entityList) {
        long start = System.currentTimeMillis();
        try (SqlSession session = SqlHelper.FACTORY.openSession(ExecutorType.BATCH)) {
            entityList.forEach(this::insert);

            session.commit();
            session.clearCache();
        }
        long end = System.currentTimeMillis();
        log.info("本次批量插入[{}]条记录,耗时{}毫秒", entityList.size(), (end - start));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Serializable id) {
        this.repo.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(QueryFilter<T> filter) {
        this.repo.delete(filter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Collection<? extends Serializable> ids) {
        this.repo.deleteBatchIds(ids);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(T entity) {

        if (entity instanceof SuperModel) {
            SuperModel e = ((SuperModel) entity);
            e.setModifyDate(new Date());
        }

        this.repo.updateById(entity);
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
        this.repo.update(entity, updateWrapper);
    }

    @Override
    @Transactional
    public int count() {
        return this.repo.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    @Transactional
    public int count(QueryWrapper<T> filter) {
        return this.repo.selectCount(filter);
    }
}
