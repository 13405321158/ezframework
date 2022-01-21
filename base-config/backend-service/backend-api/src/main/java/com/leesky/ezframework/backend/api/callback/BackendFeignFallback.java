/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:32:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api.callback;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.leesky.ezframework.backend.api.IbackendServerClient;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.json.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明：
 * <li></li>
 */
@Slf4j
@Component
public class BackendFeignFallback implements FallbackFactory<IbackendServerClient> {


    @Override
    public IbackendServerClient create(Throwable cause) {

        Result<?> ret = JSON.parseObject(cause.getMessage(), Result.class);

        return new IbackendServerClient() {
            @Override
            public Result<OauthClientDetailsDTO> getOAuth2ClientById(String clientId) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取oauthClient异常：" + ret.getMsg());
            }

            @Override
            public Result<UserAuthDTO> getUserByUsername(String username) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取username异常：" + ret.getMsg());
            }
        };
    }

}
