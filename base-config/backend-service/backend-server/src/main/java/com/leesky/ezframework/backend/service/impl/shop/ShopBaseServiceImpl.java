/*
 * @作者: 魏来
 * @日期: 2022/3/3 上午8:13
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl.shop;

import com.leesky.ezframework.backend.mapper.shop.IshopBaseMapper;
import com.leesky.ezframework.backend.model.shop.ShopBaseModel;
import com.leesky.ezframework.backend.service.shop.IshopBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/3 上午8:13
 */
@Service
@RequiredArgsConstructor
public class ShopBaseServiceImpl extends LeeskyServiceImpl<IshopBaseMapper, ShopBaseModel> implements IshopBaseService {
}
