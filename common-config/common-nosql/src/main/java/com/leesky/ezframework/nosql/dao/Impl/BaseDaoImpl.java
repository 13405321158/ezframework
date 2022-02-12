/**
 * @author: weilai
 * @Data:上午9:37:53,2019年12月19日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>描述此类的作用
 */
package com.leesky.ezframework.nosql.dao.Impl;

import com.leesky.ezframework.nosql.dao.IbaseDao;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.lang.reflect.Field;
import java.util.Map;

public class BaseDaoImpl<T, ObjectId> extends SimpleMongoRepository<T, ObjectId> implements IbaseDao<T, ObjectId> {

    private final Class<T> clazz;

    protected final MongoOperations mongoTemplate;

    protected final MongoEntityInformation<T, ObjectId> entityInformation;

    public BaseDaoImpl(MongoEntityInformation<T, ObjectId> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoTemplate = mongoOperations;
        this.entityInformation = metadata;
        clazz = entityInformation.getJavaType();
    }

    /**
     * @desc:
     * @author: weilai
     * @data: 上午9:38:53,2019年12月19日
     */
    @Override
    public void update(ObjectId id, T t) throws IllegalAccessException {
        Update update = new Update();

        Field[] field = clazz.getDeclaredFields();
        for (Field f : field) {
            f.setAccessible(true);
            Object object = f.get(t);
            if (ObjectUtils.isNotEmpty(object))
                update.set(f.getName(), object);
        }
        Query query = new Query().addCriteria(new Criteria("_id").is(id));
        this.mongoTemplate.updateFirst(query, update, clazz);
    }

    /**
     * @desc:
     * @author: weilai
     * @data: 上午9:38:53,2019年12月19日
     */
    @Override
    public void update(ObjectId id, Map<String, Object> updateFieldMap) {
        if (updateFieldMap != null && !updateFieldMap.isEmpty()) {
            Criteria criteria = new Criteria("_id").is(id);
            Update update = new Update();
            updateFieldMap.forEach(update::set);
            this.mongoTemplate.updateFirst(new Query(criteria), update, clazz);
        }
    }

    /**
     * @desc:
     * @author: weilai
     * @data: 上午9:38:53,2019年12月19日
     */
    @Override
    public void update(Map<String, Object> queryFieldMap, Map<String, Object> updateFieldMap) {
        Criteria criteria = new Criteria();
        if (MapUtils.isNotEmpty(queryFieldMap))
            queryFieldMap.forEach((key, value) -> criteria.and(key).is(value));


        if (MapUtils.isNotEmpty(updateFieldMap)) {
            Update update = new Update();
            updateFieldMap.forEach(update::set);
            this.mongoTemplate.updateFirst(new Query(criteria), update, clazz);
        }
    }
}
