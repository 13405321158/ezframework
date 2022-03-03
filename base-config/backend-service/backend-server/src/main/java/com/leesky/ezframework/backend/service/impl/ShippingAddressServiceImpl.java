/*
 * @作者: 魏来
 * @日期: 2022/3/3 下午2:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IshippingAddressMapper;
import com.leesky.ezframework.backend.model.ShippingAddressModel;
import com.leesky.ezframework.backend.service.IshippingAddressService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <li>收货地址服务实现</li>
 *
 * @author: 魏来
 * @date: 2022/3/3 下午2:22
 */
@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl extends LeeskyServiceImpl<IshippingAddressMapper, ShippingAddressModel> implements IshippingAddressService {
}
