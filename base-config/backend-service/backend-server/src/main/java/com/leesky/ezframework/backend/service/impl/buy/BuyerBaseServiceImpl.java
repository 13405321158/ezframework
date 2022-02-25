/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午2:38
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl.buy;

import com.leesky.ezframework.backend.mapper.buy.IbuyerBaseMapper;
import com.leesky.ezframework.backend.model.buyer.BuyerBaseModel;
import com.leesky.ezframework.backend.service.buy.IbuyerBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午2:38
 */
@Service
public class BuyerBaseServiceImpl extends LeeskyServiceImpl<IbuyerBaseMapper, BuyerBaseModel> implements IbuyerBaseService {
}
