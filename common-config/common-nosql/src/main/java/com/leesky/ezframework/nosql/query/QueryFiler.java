/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.nosql.query;

import com.leesky.ezframework.query.ParamModel;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class QueryFiler {

	public Query buildQuery(Criteria criteria) {
		Query query = new Query();
		query.addCriteria(criteria);
		return query;
	}

	public Query buildQuery(Criteria criteria, ParamModel model) {
		String[] list = StringUtils.split(model.getSelect(), ",");

		BasicDBObject fieldsObject = new BasicDBObject();

		for (String str : list)
			fieldsObject.put(str, 1);

		return new BasicQuery(fieldsObject.toString());
	}
}
