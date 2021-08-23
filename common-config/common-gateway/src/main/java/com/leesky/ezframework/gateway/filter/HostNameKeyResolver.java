package com.leesky.ezframework.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author: weilai
 * @Data:上午8:28:16,2019年11月26日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>根据HostName关键字限流
 *        <li>用法：
 *        <li>filters:
 *        <li>- name: RequestRateLimiter
 *        <li>args:
 *        <li>key-resolver: '#{hostNameKeyResolver}'
 *        <li>redis-rate-limiter.replenishRate: 10
 *        <li>redis-rate-limiter.burstCapacity: 20 #意味着最大并发数 20
 * 
 *        <li>ps: 哪个具体的gateway服务使用此过滤器，则需要在此服务中引用common-redis依赖
 */
public class HostNameKeyResolver implements KeyResolver {

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}

	@Bean(name = "hostNameKeyResolver")
	public HostNameKeyResolver hostAddrKeyResolver() {
		return new HostNameKeyResolver();
	}

	/**
	 * 
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 上午8:45:48,2019年11月26日
	 *
	 * @editer:
	 * @editDate: 2019年11月22日
	 * @desc:
	 *        <li>登陆用户为关键字限流
	 */
	@Bean(name = "userKeyResolver")
	KeyResolver UserKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
	}
}