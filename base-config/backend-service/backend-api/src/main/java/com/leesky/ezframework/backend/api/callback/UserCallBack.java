/*
 * @作者: 魏来
 * @日期: 2022/3/3 下午3:24
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api.callback;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.backend.api.UserClient;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.json.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/3 下午3:24
 */
@Slf4j
@Component
public class UserCallBack implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {

        Result<?> ret = JSON.parseObject(cause.getMessage(), Result.class);


        return new UserClient() {

            @Override
            public Result<UserBaseDTO> addWxUser(UserBaseDTO dto) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,增加微信用户异常：" + ret.getMsg());
            }

        };
    }
}
