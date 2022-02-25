package com.leesky.ezframework.tools.service;


import com.leesky.ezframework.nosql.service.IbaseService;
import com.leesky.ezframework.tools.model.UploadLogModel;
import org.bson.types.ObjectId;

/**
 * <li>上传到oss的对象，存储到mongodb</li>
 *
 * @author: 魏来
 * @date: 2022/2/18 下午1:46
 */
public interface IuploadLogService extends IbaseService<UploadLogModel, ObjectId> {


}
