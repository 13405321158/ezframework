/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午9:28:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.auth.details.clientdetails.ClientDetailsServiceImpl;
import com.leesky.ezframework.auth.details.userdetails.menber.MemberUserDetails;
import com.leesky.ezframework.auth.details.userdetails.user.SysUserDetails;
import com.leesky.ezframework.auth.exception.TokenEndpointFilter;
import com.leesky.ezframework.auth.ext.captcha.CaptchaTokenGranter;
import com.leesky.ezframework.auth.ext.sms.SmsCodeTokenGranter;
import com.leesky.ezframework.auth.ext.webchat.WebchatTokenGranter;
import com.leesky.ezframework.constant.RedisGlobal;
import com.leesky.ezframework.json.AjaxJson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类功能说明：
 * <li>授权中心</li>
 */

@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${access.token.validity:360}") // 默认值过期时间360
	private int accessTokenValiditySeconds;

	@Value("${access.refresh.validity:420}") // 默认值7分钟
	private int refreshTokenValiditySeconds;

	private final AuthenticationManager authenticationManager;// 在WebSecurityConfig中定义了

	private final ClientDetailsServiceImpl clientDetailsService;// 查找client服务接口

	private final LettuceConnectionFactory redisConnectionFactory;// token 存储在redis中

	/**
	 * <li>配置1=security.allowFormAuthenticationForClients();spring自带
	 * <li>配置2=ClientCredentialsTokenEndpointFilter;自定义错误返回格式
	 * <li>因为使用了【配置2】，所以注释了【配置1】
	 * <li>【配置1】实际是启用【配置2】，所以来这两个配置如果同时使用，则【配置1】覆盖【配置2】
	 * 
	 * @作者 魏来
	 * @日期 2021/12/1 下午7:15
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		TokenEndpointFilter endpointFilter = new TokenEndpointFilter(security);
		endpointFilter.afterPropertiesSet();
		endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
		security.addTokenEndpointAuthenticationFilter(endpointFilter);
	}

	/**
	 * 查询 登录时查询 client 的有效性 入口方法在此定义
	 * <li>对应数据表oauth_client_details</li>
	 *
	 * @作者 魏来
	 * @日期: 2021/12/1 上午9:16
	 */
	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(clientDetailsService);
	}

	/**
	 * 授权服务器 各类参数配置入口
	 *
	 * @作者 魏来
	 * @日期: 2021/12/1 上午9:21
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

		endpoints.tokenServices(tokenService());// 设置token属性
		endpoints.authenticationManager(authenticationManager);// 用户名和密码验证方式
		endpoints.tokenGranter(compositeTokenGranter(endpoints));// 授权方式结合点. 如果不扩展授权方式,使用系统默认,则此处可以不加
	}

	/**
	 * <li>设置token 属性
	 *
	 * @作者: 魏来
	 * @日期: 2021年11月20日 上午10:17:55
	 */
	@Bean
	public DefaultTokenServices tokenService() {
		DefaultTokenServices services = new DefaultTokenServices();

		services.setReuseRefreshToken(true);//
		services.setSupportRefreshToken(true);// 是否生成刷新token
		services.setTokenStore(tokenStore());// token存储方式：数据表中或缓存(redis)

		services.setClientDetailsService(clientDetailsService);

		services.setAccessTokenValiditySeconds(accessTokenValiditySeconds);// 设置token有效期
		services.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);

		TokenEnhancerChain extJWT = new TokenEnhancerChain();// jwt 内容扩展
		extJWT.setTokenEnhancers(Arrays.asList(jwtTokenConverter(), jwtEnhancer()));
		services.setTokenEnhancer(extJWT);

		return services;

	}

	/**
	 * @author:weilai
	 * @Data:2020年5月7日 上午9:39:43
	 * @Desc:
	 *        <li>把普通token 转换为jwt
	 */
	@Bean
	public JwtAccessTokenConverter jwtTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyPair());
		return converter;
	}
	/**
	 * 密钥库中获取密钥对(公钥+私钥)
	 */
	@Bean
	public KeyPair keyPair() {
		KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
		KeyPair keyPair = factory.getKeyPair("jwt", "123456".toCharArray());
		return keyPair;
	}
	/**
	 * token 存储到redis中
	 * <li>token 存储方式有三：1数据表、2jwt、3redis</li>
	 * <li>这里采用存储到redis中，好处时发放的token可以收回</li>
	 *
	 * @author： 魏来
	 * 
	 * @date: 2021/12/1 上午9:31
	 */
	@Bean
	public RedisTokenStore tokenStore() {
		RedisTokenStore redisStore = new RedisTokenStore(redisConnectionFactory);
		redisStore.setPrefix(RedisGlobal.AUTH_TOKEN);
		return redisStore;
	}

	/**
	 * 扩展jwt内容
	 */
	@Bean
	public TokenEnhancer jwtEnhancer() {
		return (accessToken, authentication) -> {
			Map<String, Object> additionalInfo = Maps.newHashMap();
			Object principal = authentication.getUserAuthentication().getPrincipal();

			if (principal instanceof SysUserDetails) {
				SysUserDetails sysUserDetails = (SysUserDetails) principal;
				additionalInfo.put("userId", sysUserDetails.getUserId());
				additionalInfo.put("username", sysUserDetails.getUsername());
				if (StringUtils.isNotBlank(sysUserDetails.getAuthenticationMethod()))
					additionalInfo.put("authenticationMethod", sysUserDetails.getAuthenticationMethod());

			}

			if (principal instanceof MemberUserDetails) {
				MemberUserDetails memberUserDetails = (MemberUserDetails) principal;
				additionalInfo.put("userId", memberUserDetails.getUserId());
				additionalInfo.put("username", memberUserDetails.getUsername());
				if (StringUtils.isNotBlank(memberUserDetails.getAuthenticationMethod()))
					additionalInfo.put("authenticationMethod", memberUserDetails.getAuthenticationMethod());

			}

			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}

	/**
	 * <li>扩展授权方式,比如增加:微信登录\SMS登录\QQ登录|自定义的二维码登录等
	 *
	 * @作者: 魏来
	 * @日期: 2021年11月20日 上午10:17:55
	 */
	private CompositeTokenGranter compositeTokenGranter(AuthorizationServerEndpointsConfigurer point) {

		// 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
		List<TokenGranter> list = Lists.newArrayList(point.getTokenGranter());

		OAuth2RequestFactory factory = point.getOAuth2RequestFactory();
		ClientDetailsService clientDetails = point.getClientDetailsService();
		AuthorizationServerTokenServices tokenService = point.getTokenServices();

		// 添加验证码授权模式授权者
		list.add(new CaptchaTokenGranter(tokenService, clientDetails, factory, authenticationManager// , stringRedisTemplate
		));
		// 添加手机短信验证码授权模式的授权者
		list.add(new SmsCodeTokenGranter(tokenService, clientDetails, factory, authenticationManager));
		// 添加微信授权模式的授权者
		list.add(new WebchatTokenGranter(tokenService, clientDetails, factory, authenticationManager));

		return new CompositeTokenGranter(list);
	}

	/**
	 * 自定义认证异常响应数据
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			AjaxJson<?> json = new AjaxJson<>(false, "client_id和client_secret不匹配");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);// 固定返回200(网络层正常[response.getStatus()实际状态])

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), json);
		};
	}


}
