package com.leesky.ezframework.tools.action;

import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.redis.service.RedisService;
import com.leesky.ezframework.tools.utils.AlyUtil;
import com.leesky.ezframework.tools.utils.MbltUtil;
import com.leesky.ezframework.tools.utils.SignEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SendAction {


    private final RedisService cache;

    private final AlyUtil alyUtil;
    private final MbltUtil mbltUtil;


    /**
     * <li></li>
     *
     * @author: 魏来
     * @date: 2022/2/25 下午12:47
     */
    @SneakyThrows
    @PostMapping("/mb01/{mobile}/public")
    public Result<?> mb(@PathVariable String mobile) {
        Object obj = this.cache.get(Redis.SMS_KEY + mobile);
        Assert.isTrue(ObjectUtils.isEmpty(obj), "SMS发送频率过于频繁:" + mobile);

        String code = RandomStringUtils.randomNumeric(4);
        mbltUtil.send(mobile, "验证码:" + code);
        this.cache.add(Redis.SMS_KEY + mobile, code, 60L);

        return Result.success();
    }

    /**
     * desc
     *
     * @author： 魏来
     * @date: 2022/2/24  下午6:50
     */
    @PostMapping("/ali01/{mobile}/public")
    public Result<?> r01(@PathVariable String mobile) {
        Object obj = this.cache.get(Redis.SMS_KEY + mobile);
        Assert.isTrue(ObjectUtils.isEmpty(obj), "SMS发送频率过于频繁:" + mobile);

        String code = RandomStringUtils.randomNumeric(4);
        alyUtil.send(mobile, code, SignEnum.qlb);
        this.cache.add(Redis.SMS_KEY + mobile, code, 60L);


        return Result.success();
    }
}
