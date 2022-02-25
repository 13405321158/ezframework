/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午2:37
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl.saler;

import com.leesky.ezframework.backend.mapper.saler.IsalerBaseMapper;
import com.leesky.ezframework.backend.model.saler.SalerBaseModel;
import com.leesky.ezframework.backend.service.saler.IsalerBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午2:37
 */
@Service
public class SalerBaseServiceImpl extends LeeskyServiceImpl<IsalerBaseMapper, SalerBaseModel> implements IsalerBaseService {
}
