package com.leesky.ezframework.gateway.filter;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.google.common.collect.Maps;
import com.leesky.ezframework.gateway.utils.SHAUtil;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * desc：url头部参数检查
 *
 * @author： 魏来
 * @date： 2021/12/8 上午10:41
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HeaderParamFilter implements GlobalFilter {

    private String uuid01 = "uid1";//登录者id
    private String uuid02 = "uid2";//随机数
    private String uuid03 = "uid3";//时间戳
    private String uuid04 = "uid4";//签名


    private final RedisService cache;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        try {
            String uid = exchange.getRequest().getHeaders().getFirst(uuid01);//用户id
            String random = exchange.getRequest().getHeaders().getFirst(uuid02);//随机数
            String timestamp = exchange.getRequest().getHeaders().getFirst(uuid03);//时间戳
            String sign = exchange.getRequest().getHeaders().getFirst(uuid04);//签名
            //1、检查头部参数是否齐全
            checkParam(exchange, uid, random, timestamp, sign);

            //2、验签
            Boolean serverSign = SHAUtil.disEncode(uid + random + timestamp, sign);
            Assert.isTrue(serverSign, "验签失败,判为重放攻击");

            //3、是否超时
            long currTime = System.currentTimeMillis();
            long clientTimes = Long.parseLong(timestamp);
            Assert.isTrue((currTime - clientTimes) < 60000, "访问时间戳超时>60秒:" + (currTime - clientTimes) + ",系统判定：重放攻击");

            //4、url是否使用过
            String key = "[nonce" + random + "]_" + uid;
            String value = (String) this.cache.get(key);
            this.cache.add(key, "used", 3 * 60L);
            Assert.isTrue(StringUtils.isBlank(value), "nonce[" + random + "] 被使用过,系统判定：重放攻击");

            //5、获取token并增加到head参数中
            Object token = this.cache.get("auth-token-id_" + uid);
            Assert.isTrue(ObjectUtils.isNotEmpty(token), "令牌已失效或用户未登录,请登录重试");

            ServerHttpRequest request = exchange.getRequest().mutate().header("Authorization", "bearer " + token).build();

            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return exchange.getResponse().writeWith(Mono.just(data(exchange, path, e.getMessage())));
        }

    }

    /**
     * 捕获异常信息输出
     *
     * @author： 魏来
     * @date: 2021/12/8 下午3:53
     */
    private DataBuffer data(ServerWebExchange exchange, String path, String ex) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

        Map<String, Object> map = Maps.newHashMap();
        map.put("msg", ex);
        map.put("path", path);
        map.put("success", false);
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("statusCode", exchange.getResponse().getStatusCode());

        return response.bufferFactory().wrap(new Gson().toJson(map).getBytes());
    }

    /**
     * 检查url的头部参数
     *
     * @author： 魏来
     * @date: 2021/12/8 上午11:17
     */
    private void checkParam(ServerWebExchange exchange, String uid, String random, String stamp, String sign) {
        Assert.isTrue(StringUtils.isNotBlank(uid), ":header参数uid1缺失");
        Assert.isTrue(StringUtils.isNotBlank(random), ":header参数uid2缺失");
        Assert.isTrue(StringUtils.isNotBlank(stamp), ":header参数uid3缺失");
        Assert.isTrue(StringUtils.isNotBlank(sign), ":header参数uid4缺失");
    }

}
