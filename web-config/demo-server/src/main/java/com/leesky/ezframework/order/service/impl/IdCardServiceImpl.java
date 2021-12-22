/*
 * @作者: 魏来
 * @日期: 2021/10/13  下午3:28
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.IidCardMapper;
import com.leesky.ezframework.order.model.IdCardModel;
import com.leesky.ezframework.order.service.IidCardService;
import org.springframework.stereotype.Service;

/**
 * <li>描述:
 */
@Service
public class IdCardServiceImpl extends LeeskyServiceImpl<IidCardMapper, IdCardModel> implements IidCardService {
}
