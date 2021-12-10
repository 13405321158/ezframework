/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:32:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api.callback;

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

        return new IbackendServerClient() {
            @Override
            public Result<OauthClientDetailsDTO> getOAuth2ClientById(String clientId) {
                log.error(cause.getMessage());
                return Result.failed("远程调用backend-server服务降级，获取oauthClient失败:" + clientId);
            }

            @Override
            public Result<UserAuthDTO> getUserByUsername(String username) {
                log.error(cause.getMessage());
                return Result.failed("远程调用backend-server服务降级，获取username失败:" + username);
            }
        };
    }

}
