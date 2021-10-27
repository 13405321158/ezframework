/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午3:22:58
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IgroupMapper;
import com.leesky.ezframework.backend.model.GroupModel;
import com.leesky.ezframework.backend.service.IgroupService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends LeeskyServiceImpl<IgroupMapper, GroupModel> implements IgroupService {

}
