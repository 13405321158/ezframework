package com.leesky.ezframework.nosql.dao.Impl;

import com.leesky.ezframework.nosql.dao.IbaseDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.lang.reflect.Field;
import java.util.Map;



/**
 * 
 * @author: weilai
 * @Data:上午9:37:53,2019年12月19日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>描述此类的作用
 */
public class BaseDaoImpl<T, ObjectId> extends SimpleMongoRepository<T, ObjectId> implements IbaseDao<T, ObjectId> {

	private Class<T> clazz;

	protected final MongoOperations mongoTemplate;

	protected final MongoEntityInformation<T, ObjectId> entityInformation;

	public BaseDaoImpl(MongoEntityInformation<T, ObjectId> metadata, MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
		this.mongoTemplate = mongoOperations;
		this.entityInformation = metadata;
		clazz = entityInformation.getJavaType();
	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:上午9:38:53,2019年12月19日
	 * @desc:
	 *        <li>
	 *
	 */
	@Override
	public void update(ObjectId id, T t) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(new Criteria("_id").is(id));
		Field[] field = clazz.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			Field f = field[i];
			f.setAccessible(true);
			try {
				Object object = f.get(t);
				if (object != null) {
					update.set(f.getName(), object);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		this.mongoTemplate.updateFirst(query, update, clazz);
	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:上午9:39:06,2019年12月19日
	 * @desc:
	 *        <li>
	 *
	 */
	@Override
	public void update(ObjectId id, Map<String, Object> updateFieldMap) {
		if (updateFieldMap != null && !updateFieldMap.isEmpty()) {
			Criteria criteria = new Criteria("_id").is(id);
			Update update = new Update();
			updateFieldMap.forEach(update::set);
			mongoTemplate.updateFirst(new Query(criteria), update, clazz);
		}
	}

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:上午9:39:16,2019年12月19日
	 * @desc:
	 *        <li>
	 *
	 */
	@Override
	public void update(Map<String, Object> queryFieldMap, Map<String, Object> updateFieldMap) {
		Criteria criteria = new Criteria();
		if (null != queryFieldMap && !queryFieldMap.isEmpty()) {
			queryFieldMap.forEach((key, value) -> criteria.and(key).is(value));
		}

		if (updateFieldMap != null && !updateFieldMap.isEmpty()) {
			Update update = new Update();
			updateFieldMap.forEach(update::set);
			mongoTemplate.updateFirst(new Query(criteria), update, clazz);
		}
	}
}
