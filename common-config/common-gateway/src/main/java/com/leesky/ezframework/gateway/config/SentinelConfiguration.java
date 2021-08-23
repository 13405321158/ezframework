package com.leesky.ezframework.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SentinelConfiguration {

	private final List<ViewResolver> viewResolvers;
	private final ServerCodecConfigurer serverCodecConfigurer;

	public SentinelConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@Bean
	@Order(-1)
	public GlobalFilter sentinelGatewayFilter() {
		return new SentinelGatewayFilter();
	}

	@PostConstruct
	public void doInit() {
		initCustomizedApis();
		initGatewayRules();
	}

	// ApiDefinition：用户自定义的 API 定义分组，可以看做是一些 URL 匹配的组合。
	// 比如我们可以定义一个 API 叫 my_api，请求 path 模式为 /foo/ 和 /baz/ 的都归到 my_api 这个 API 分组下面。
	// 限流的时候可以针对这个自定义的 API 分组维度进行限流
	private void initCustomizedApis() {
		Set<ApiDefinition> definitions = Sets.newHashSet();
		ApiDefinition api1 = new ApiDefinition("some_customized_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			private static final long serialVersionUID = 6829123876440229738L;

			{
				add(new ApiPathPredicateItem().setPattern("/ahas"));
				add(new ApiPathPredicateItem().setPattern("/product/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		ApiDefinition api2 = new ApiDefinition("another_customized_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			private static final long serialVersionUID = -103215960191557987L;

			{
				add(new ApiPathPredicateItem().setPattern("/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		definitions.add(api1);
		definitions.add(api2);
		GatewayApiDefinitionManager.loadApiDefinitions(definitions);
	}

	// GatewayFlowRule:网关限流规则：
	// 针对 API Gateway 的场景定制的限流规则，可以针对不同 route 或自定义的 API 分组进行限流
	// 支持针对请求中的参数、Header、来源 IP 等进行定制化的限流。
	private void initGatewayRules() {
		Set<GatewayFlowRule> rules = new HashSet<>();
		rules.add(new GatewayFlowRule("aliyun_route").setCount(10).setIntervalSec(1));
		rules.add(new GatewayFlowRule("aliyun_route").setCount(2).setIntervalSec(2).setBurst(2).setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(10).setIntervalSec(1).setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER).setMaxQueueingTimeoutMs(600)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER).setFieldName("X-Sentinel-Flag")));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(1).setIntervalSec(1).setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("pa")));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(2).setIntervalSec(30).setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("type")
				.setPattern("warn").setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)));

		rules.add(new GatewayFlowRule("some_customized_api").setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME).setCount(5).setIntervalSec(1)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("pn")));
		GatewayRuleManager.loadRules(rules);
	}
}
