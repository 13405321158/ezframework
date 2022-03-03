/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:35:01
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api;

import com.leesky.ezframework.backend.api.callback.LoginCallBack;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.feign.FeignErrorDecoder;
import com.leesky.ezframework.json.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "backend-server", contextId = "login", fallbackFactory = LoginCallBack.class, configuration = {FeignErrorDecoder.class})
public interface LoginClient {

    /**
     * 登录时查询client
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:18
     */
    @GetMapping("/login/client")
    Result<OauthClientDetailsDTO> getClient(@RequestParam String clientId);

    /**
     * 查询系统用户名
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:46
     */
    @GetMapping("/login/sys/{var}/{type}")
    Result<UserBaseDTO> loadSys(@PathVariable String var, @PathVariable String type);

    /**
     * 查询买家（C端）用户
     *
     * @author: 魏来
     * @date: 2022/2/24 上午9:40
     */
    @GetMapping("/login/buy/{var}/{type}")
    Result<UserBaseDTO> loadBuyer(@PathVariable String var, @PathVariable String type);

    /**
     * 查询代理商用户
     *
     * @author: 魏来
     * @date: 2022/2/24 上午9:41
     */
    @GetMapping("/login/sale/{var}/{type}")
    Result<UserBaseDTO> loadSaler(@PathVariable String var, @PathVariable String type);

    /**
     * 查询代理商用户
     *
     * @author: 魏来
     * @date: 2022/2/24 上午9:41
     */
    @GetMapping("/login/dealer/{var}/{type}")
    Result<UserBaseDTO> loadDealer(@PathVariable String var, @PathVariable String type);


}
