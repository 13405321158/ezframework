package com.leesky.ezframework.gateway.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * @author weilai
 * @data 2019年2月22日 下午4:04:29
 *
 * @desc 类描述
 *       <li>网关跨域设置。说明：
 *       <li>如果不使用网关springGate，则在工程security-common的配置类MvcConfigurer注释掉@Configuration
 *       <li>同时在工程security-common的配置类OAuth2ResourceServerConfiguration注释掉http.csrf().disable().cors()
 *       <li>如果不注释掉上面两个，则浏览器会提示：有多个Access-Control-Allow-Origin等
 */
@Configuration
public class RouteConfiguration {

	private static final String ALL = "*";
	private static final String MAX_AGE = "18000L";
	private static final String CREDENTIALS = "true";

	public static final String WEWB_SOCKET_SERVER_NAME = "/web-socket";// websocket 服务名称

	@Bean
	public WebFilter corsFilter() {
		return (ServerWebExchange ctx, WebFilterChain chain) -> {
			ServerHttpRequest request = ctx.getRequest();
			String webPath = request.getURI().getPath();
			if (CorsUtils.isCorsRequest(request)) {
				ServerHttpResponse response = ctx.getResponse();

				HttpHeaders requestHeaders = request.getHeaders();

				HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
				HttpHeaders headers = response.getHeaders();
				
				headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
				headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
				
				if (!StringUtils.startsWith(webPath, WEWB_SOCKET_SERVER_NAME)) {
					headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, CREDENTIALS);
					headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
				}
				headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
				
				if (requestMethod != null)
					headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());

				if (request.getMethod() == HttpMethod.OPTIONS) {
					response.setStatusCode(HttpStatus.OK);
					return Mono.empty();
				}
			}

			return chain.filter(ctx);
		};
	}

}
