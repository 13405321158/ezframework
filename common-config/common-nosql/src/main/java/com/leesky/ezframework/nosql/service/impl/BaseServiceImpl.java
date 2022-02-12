/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.nosql.service.impl;

import com.leesky.ezframework.nosql.dao.IbaseDao;
import com.leesky.ezframework.nosql.query.CriteriaParam;
import com.leesky.ezframework.nosql.query.QueryFiler;
import com.leesky.ezframework.nosql.service.IbaseService;
import com.leesky.ezframework.query.ParamModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class BaseServiceImpl<E, ObjectId> implements IbaseService<E, ObjectId> {

    @Autowired
    private IbaseDao<E, ObjectId> repo;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Class<E> clz;

    public Class<E> getClz() {
        if (clz == null) {
            clz = (Class<E>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        return clz;
    }

    @Override
    public List<E> list() {

        return this.repo.findAll();
    }


    @Override
    public List<E> list(ParamModel model) {
        Query query;
        QueryFiler filter = new QueryFiler();

        CriteriaParam params = new CriteriaParam(model);

        if (StringUtils.isNotBlank(model.getSelect()))
            query = filter.buildQuery(params.make(), model);
        else
            query = filter.buildQuery(params.make());

        return this.mongoTemplate.find(query, getClz());
    }


    @Override
    public List<E> findById(List<ObjectId> pk) {

        return (List<E>) this.repo.findAllById(pk);

    }

    @Override
    public E findById(ObjectId pk) {

        return (E) this.repo.findById(pk);
    }




    @Override
    public Page<E> page(ParamModel model) {
        Pageable pageable = PageRequest.of(model.getPage() - 1, model.getLimit(), Direction.ASC, "id");
        var params = new CriteriaParam(model);

        QueryFiler filter = new QueryFiler();
        Query query = filter.buildQuery(params.make());
        long total = this.mongoTemplate.count(query, getClz());
        List<E> items = this.mongoTemplate.find(query.with(pageable), getClz());
        return new PageImpl<>(items, pageable, total);
    }

    @Override
    public E save(E entity) {

        return this.repo.save(entity);
    }


    @Override
    public void saveBatch(List<E> list) {
        this.repo.saveAll(list);

    }


    @Override
    public void deleteById(ObjectId pk) {
        this.repo.deleteById(pk);

    }


    @Override
    public void update(ObjectId id, E t) throws IllegalAccessException {
        this.repo.update(id, t);
    }

    @Override
    public void update(ObjectId id, Map<String, Object> updateFieldMap) {
        this.repo.update(id, updateFieldMap);
    }

    @Override
    public void update(Map<String, Object> queryFieldMap, Map<String, Object> updateFieldMap) {
        this.repo.update(queryFieldMap, updateFieldMap);

    }

}
