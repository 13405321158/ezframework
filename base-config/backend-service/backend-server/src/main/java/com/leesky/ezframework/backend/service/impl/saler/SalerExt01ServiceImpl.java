/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午3:03
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl.saler;

import com.leesky.ezframework.backend.mapper.saler.IsalereExt01Mapper;
import com.leesky.ezframework.backend.model.saler.SalerExt01Model;
import com.leesky.ezframework.backend.service.saler.IsalerExt01Service;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午3:03
 */
@Service
public class SalerExt01ServiceImpl extends LeeskyServiceImpl<IsalereExt01Mapper, SalerExt01Model> implements IsalerExt01Service {
}
