
/**
 * @author weilai
 * @data 2018年11月20日 下午3:47:29
 *
 * @desc 类描述
 *       <li>
 */package com.leesky.ezframework.log.service;


import com.leesky.ezframework.log.model.SysLogModel;
import com.leesky.ezframework.nosql.service.IbaseService;
import org.bson.types.ObjectId;


public interface IsyslogService extends IbaseService<SysLogModel, ObjectId> {

}
