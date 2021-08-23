package com.leesky.ezframework.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author: weilai
 * @Data:上午8:38:38,2019年11月26日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 * <li>根据uri去限流
 */

public class UriKeyResolver implements KeyResolver {

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		return Mono.just(exchange.getRequest().getURI().getPath());
	}

	@Bean
	public UriKeyResolver uriKeyResolver() {
		return new UriKeyResolver();
	}
}