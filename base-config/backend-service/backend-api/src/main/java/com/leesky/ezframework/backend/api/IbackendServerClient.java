/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:35:01
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api;

import com.leesky.ezframework.backend.api.callback.BackendFeignFallback;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.feign.FeignErrorDecoder;
import com.leesky.ezframework.json.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 类功能说明：
 * <li></li>
 */

@FeignClient(value = "backend-server", fallbackFactory = BackendFeignFallback.class, configuration = {FeignErrorDecoder.class})
public interface IbackendServerClient {

    /**
     * 登录时查询client
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:18
     */
    @GetMapping("/client/r01/public")
    Result<OauthClientDetailsDTO> getOAuth2ClientById(@RequestParam String clientId);

    /**
     * 登录时查询系统用户名
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:46
     */
    @GetMapping("/sys-user/{username}/public")
    Result<UserBaseDTO> loadSystemUserByUsername(@PathVariable String username);

    /**
     * 查询买家用户
     *
     * @author: 魏来
     * @date: 2022/2/24 上午9:40
     */
    @GetMapping("/buyer-user/{username}/public")
    Result<UserBaseDTO> loadBuyerUserByUsername(@PathVariable String username);

    /**
     * 查询买家用户
     *
     * @author: 魏来
     * @date: 2022/2/24 上午9:41
     */
    @GetMapping("/sale-user/{username}/public")
    Result<UserBaseDTO> loadSalerUserByUsername(@PathVariable String username);
}
