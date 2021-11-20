/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:35:01
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.leesky.ezframework.backend.api.callback.BackendFeignFallback;

/**
 * 类功能说明：
 * <li></li>
 */

@FeignClient(value = "backend-server", fallbackFactory = BackendFeignFallback.class)
public interface IbackendServerClient {

}
