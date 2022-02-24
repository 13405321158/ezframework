/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午10:36
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: <li>验证码控制器</li>
 */
package com.leesky.ezframework.backend.action;

import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.redis.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateAction {

    private final RedisService cache;

    /**
     * 图片登录验证码
     *
     * @author： 魏来
     * @date: 2022/2/16  下午4:14
     */
    @GetMapping(value = "/img/public")
    public Result<Map<String, String>> ValidateCode() {

        String uuid = UUID.randomUUID().toString().replace("-", "");
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);//图片size

        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        String text = captcha.text();// 获取运算的结果：5

        this.cache.add(Redis.LOGIN_IMG_CODE + uuid, text, 60 * 60L);

        ImmutableMap<String, String> data = ImmutableMap.of("key", uuid, "codeUrl", captcha.toBase64());

        return Result.success(data, false);
    }
}
