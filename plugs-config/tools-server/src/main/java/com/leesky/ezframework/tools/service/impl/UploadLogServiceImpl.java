/*
 * @作者: 魏来
 * @日期: 2022/2/18 下午1:47
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.tools.service.impl;


import com.leesky.ezframework.nosql.service.impl.BaseServiceImpl;
import com.leesky.ezframework.tools.model.UploadLogModel;
import com.leesky.ezframework.tools.service.IuploadLogService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/18 下午1:47
 */
@Service
public class UploadLogServiceImpl extends BaseServiceImpl<UploadLogModel, ObjectId> implements IuploadLogService {
}
