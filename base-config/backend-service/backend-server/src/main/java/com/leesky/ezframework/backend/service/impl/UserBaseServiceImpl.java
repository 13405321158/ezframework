/*
 * @作者: 魏来
 * @日期: 2021-08-21 15:48:39
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IuserBaseMapper;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserBaseServiceImpl extends LeeskyServiceImpl<IuserBaseMapper, UserBaseModel> implements IuserBaseService {


}
