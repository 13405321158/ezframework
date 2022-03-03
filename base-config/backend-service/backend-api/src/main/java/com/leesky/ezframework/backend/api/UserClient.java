package com.leesky.ezframework.backend.api;

import com.leesky.ezframework.backend.api.callback.UserCallBack;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.feign.FeignErrorDecoder;
import com.leesky.ezframework.json.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/3 下午3:23
 */
@FeignClient(value = "backend-server", contextId = "user", fallbackFactory = UserCallBack.class, configuration = {FeignErrorDecoder.class})
public interface UserClient {


    /**
     * <li>增加微信小程序类用户</li>
     *
     * @author: 魏来
     * @date: 2022/3/2 下午4:19
     */
    @GetMapping("/buyer-user/c01")
    Result<UserBaseDTO> addWxUser(@RequestBody UserBaseDTO dto);


}
