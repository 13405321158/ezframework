/*
 * @作者: 魏来
 * @日期: 2021/10/15  下午4:53
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.ItelMapper;
import com.leesky.ezframework.order.model.TelModel;
import com.leesky.ezframework.order.service.ItelService;
import org.springframework.stereotype.Service;

/**
 * <li>描述:
 */
@Service
public class TelServiceImpl extends LeeskyServiceImpl<ItelMapper, TelModel> implements ItelService {
}
