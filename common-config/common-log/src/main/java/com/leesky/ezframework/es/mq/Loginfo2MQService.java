/*
 * @作者: 魏来
 * @日期: 2022/2/11 上午11:23
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.es.mq;


import com.leesky.ezframework.stream.pipeline.OutputChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Loginfo2MQService {

    private final StreamBridge streamBridge;

    /**
     * <li>消息生产者：生成的日志信息 发送到MQ的：LEESKY_01_EXCHANGE，在common-stream-dev.yaml配置了leesky01-out-0</li>
     * <li>如果MQ宕机，则消息是无法发送的，解决方案01： 把消息扔到缓存中，比如ES，然后通过定时任务去发送消息，如果接不到回执，则继续发送</li>
     *
     * @author: 魏来
     * @date: 2022/2/12 下午1:14
     */
    public void sendLog(String msg) {
//        Message<String> msg = MessageBuilder.withPayload(msg).setHeader("k3Dispatcher-header", "dispatcher").build();
        this.streamBridge.send(OutputChannel.LEESKY_OUTPUT01, msg);
    }

}
