/*
 * @作者: 魏来
 * @日期: 2021-08-27 15:02:02
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc: 
 */
package com.leesky.ezframework.backend.service.impl;

import org.springframework.stereotype.Service;
import com.leesky.ezframework.service.impl.BaseServiceImpl;
import com.leesky.ezframework.backend.model.DealerOrderModel;
import com.leesky.ezframework.backend.service.IdealerOrderService;
import com.leesky.ezframework.backend.mapper.IdealerOrderMapper;

@Service
public class DealerOrderServiceImpl extends BaseServiceImpl<IdealerOrderMapper,DealerOrderModel>implements IdealerOrderService {
}
