/**
 * @Data:上午10:43:12 , 2019年11月16日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc:
 */
package com.leesky.ezframework.log.mq;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.dto.SysLogDTO;
import com.leesky.ezframework.log.model.SysLogModel;
import com.leesky.ezframework.log.service.IsyslogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class LogMQReciverService {

    private final IsyslogService service;


    /**
     * <li>从 leesky01-in-0 通道中 接受日志消息； 在log-config-dev.yaml 定义了【leesky01-in-0】
     * ps：注意这里  @Bean("leesky01")，必须是leesky01，换个名就不好用
     *
     * @author: 魏来
     * @date: 2022/2/10 下午2:00
     */
    @Bean("leesky01")
    public Consumer<String> accept() {
        return msg -> {
            SysLogDTO json=JSON.parseObject(msg,SysLogDTO.class);

            SysLogModel entity = new SysLogModel(json);
            this.service.save(entity);
        };


    }

}
