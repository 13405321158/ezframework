package com.leesky.ezframework.log.dao;


import com.leesky.ezframework.log.model.SysLogModel;
import com.leesky.ezframework.nosql.dao.IbaseDao;
import org.bson.types.ObjectId;

/**
 * 
 * @author weilai
 * @data 2018年11月21日 上午11:54:40
 *
 * @desc 类描述
 *       <li>仓库接口，不用添加注解
 *       <li>Spring data jpa的那一套查询规则，在MongoDB仍然适用:约定大于实现
 */
public interface IsysLogRepo extends IbaseDao<SysLogModel, ObjectId> {
}
