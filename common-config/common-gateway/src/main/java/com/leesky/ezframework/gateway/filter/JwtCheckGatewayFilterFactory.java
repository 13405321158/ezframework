package com.leesky.ezframework.gateway.filter;

/**
 * 
 * @author: weilai
 * @Data:上午8:20:00,2019年11月26日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>检查访问url是否放行，放行条件：1、带有token；2、或者是白名单内url。
 */
//@Component
//public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {
//
//	public JwtCheckGatewayFilterFactory() {
//		super(Config.class);
//	}
//
//	@Override
//	public GatewayFilter apply(Config config) {
//		return (exchange, chain) -> {
//			String path = exchange.getRequest().getURI().getPath();
//			String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
//			String expectWhite = StringUtils.split(path, "/")[1];
//
//			boolean image = expectWhite.equals(Global.IMAGE_LIST_PREFIX) ? true : false;
//			boolean whiteList = expectWhite.equals(Global.WHITE_LIST_PREFIX) ? true : false;
//  
//			if (jwtToken != null || whiteList || image)
//				return chain.filter(exchange);
//
//			// 不合法(响应未登录的异常)
//			ServerHttpResponse response = exchange.getResponse();
//			HttpHeaders httpHeaders = response.getHeaders();
//			httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
//			httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
//
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("success", false);
//			map.put("path", path);
//			map.put("msg", "访问路径不在白名单内,并且访问没有携带到令牌");
//			map.put("Tips", "Sentury Tire Co., Ltd");
//			map.put("timestamp", LocalDateTime.now().toString());
//			map.put("statusCode", 401);
//
//			DataBuffer bodyDataBuffer = response.bufferFactory().wrap(new Gson().toJson(map).getBytes());
//
//			return response.writeWith(Mono.just(bodyDataBuffer));
//		};
//	}
//
//	public static class Config {
//
//	}
//}
