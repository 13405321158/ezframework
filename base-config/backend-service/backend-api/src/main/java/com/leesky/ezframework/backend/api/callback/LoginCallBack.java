/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:32:12
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.api.callback;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.backend.api.LoginClient;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.json.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LoginCallBack implements FallbackFactory<LoginClient> {


    @Override
    public LoginClient create(Throwable cause) {

        Result<?> ret = JSON.parseObject(cause.getMessage(), Result.class);

        return new LoginClient() {
            @Override
            public Result<OauthClientDetailsDTO> getClient(String clientId) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取oauthClient异常：" + ret.getMsg());
            }

            @Override
            public Result<UserBaseDTO> loadSys(String var, String type) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取系统用户异常：" + ret.getMsg());
            }
            @Override
            public Result<UserBaseDTO> loadBuyer(String var, String type) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取买家用户异常：" + ret.getMsg());
            }
            @Override
            public Result<UserBaseDTO> loadSaler(String var, String type) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取卖家用户异常：" + ret.getMsg());
            }

            @Override
            public Result<UserBaseDTO> loadDealer(String var, String type) {
                log.error(ret.getMsg());
                return Result.failed("backend-server服务降级,获取经销商用户异常：" + ret.getMsg());
            }




        };
    }

}
