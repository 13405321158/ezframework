/*
 * @作者: 魏来
 * @日期: 2021-08-21 15:48:39
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc: 
 */
package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IuserBaseExt02Mapper;
import com.leesky.ezframework.backend.model.sys.UserBaseExt02Model;
import com.leesky.ezframework.backend.service.IuserBaseExt02Service;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserBaseExt02ServiceImpl extends LeeskyServiceImpl<IuserBaseExt02Mapper, UserBaseExt02Model> implements IuserBaseExt02Service {
}
