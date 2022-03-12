package com.leesky.ezframework.gateway.filter;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.google.common.collect.Maps;
import com.leesky.ezframework.gateway.utils.SHAUtil;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
public class HeaderParamFilter implements GlobalFilter {
    @Autowired
    private RedisService cache;

    private final String URL_SUFFIX = "/**/public";
    private final String OAUTH_PREFIX = "/oauth/*";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        try {
            //如果是白名单，则直接放行
            boolean b1 = pathMatcher.match(URL_SUFFIX, path);
            boolean b2 = pathMatcher.match(OAUTH_PREFIX, path);
            if (b1 || b2)
                return chain.filter(exchange);

            String uid = exchange.getRequest().getHeaders().getFirst("uid1");//用户id
            String random = exchange.getRequest().getHeaders().getFirst("uid2");//随机数
            String timestamp = exchange.getRequest().getHeaders().getFirst("uid3");//时间戳
            String sign = exchange.getRequest().getHeaders().getFirst("uid4");//签名

            //1、检查头部参数是否齐全
            checkParam(exchange, uid, random, timestamp, sign);

            //2、验签
            Boolean serverSign = SHAUtil.disEncode(uid + random + timestamp, sign);
            Assert.isTrue(serverSign, "验签失败: " + uid);

            //3、是否超时:默认10秒
            long currTime = System.currentTimeMillis();
            long clientTimes = Long.parseLong(timestamp);
            Assert.isTrue((currTime - clientTimes) < 10000, "访问超时,系统判定重放攻击: " + uid);

            //4、url是否使用过
            String key = "[nonce" + random + "]_" + uid;
            String value = (String) this.cache.get(key);
            this.cache.add(key, "used",  15L);
            Assert.isTrue(StringUtils.isBlank(value), "url已使用,系统判定,重放攻击: " + uid);

            //5、获取token
            Object token = this.cache.get(Redis.AUTH_TOKEN_ID + uid);
            Assert.isTrue(ObjectUtils.isNotEmpty(token), "令牌已失效或用户未登录,请登录重试，登录Id：" + uid);

            //6、url头部增加参数 Authorization
            ServerHttpRequest request = exchange.getRequest().mutate().header(Common.URL_HEADER_PARAM, Common.TOKEN_TYPE + token).build();
            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            log.error(e.getMessage());
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

        response.setStatusCode(HttpStatus.OK);

        return response.bufferFactory().wrap(new Gson().toJson(map).getBytes());
    }

    /**
     * 检查url的头部参数
     *
     * @author： 魏来
     * @date: 2021/12/8 上午11:17
     */
    private void checkParam(ServerWebExchange exchange, String uid, String random, String stamp, String sign) {
        Assert.isTrue(StringUtils.isNotBlank(uid), "header参数uid1缺失");
        Assert.isTrue(StringUtils.isNotBlank(random), "header参数uid2缺失" + uid);
        Assert.isTrue(StringUtils.isNotBlank(stamp), "header参数uid3缺失" + uid);
        Assert.isTrue(StringUtils.isNotBlank(sign), "header参数uid4缺失" + uid);
    }

}
