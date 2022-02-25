package com.leesky.ezframework.tools.repo;


import com.leesky.ezframework.nosql.dao.IbaseDao;
import com.leesky.ezframework.tools.model.UploadLogModel;
import org.bson.types.ObjectId;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/18 下午2:01
 */
public interface IuploadLogRepo extends IbaseDao<UploadLogModel, ObjectId> {
}
