/**
 * 
 * @author:weilai
 * @Data:09-Sep-202014:19:37
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.gateway.config;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Component
public class WebsocketHandler implements GlobalFilter, Ordered {

	private final static String DEFAULT_FILTER_PATH = "/web-socket/stomp";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

		String scheme = requestUrl.getScheme();

		if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
			return chain.filter(exchange);
		} else if (DEFAULT_FILTER_PATH.equals(requestUrl.getPath())) {
			String wsScheme = convertWsToHttp(scheme);
			URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build().toUri();
			exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 2;
	}

	static String convertWsToHttp(String scheme) {
		scheme = scheme.toLowerCase();
		return "ws".equals(scheme) ? "http" : "wss".equals(scheme) ? "https" : scheme;
	}

}
