/*
  @author:weilai
 * @Data:2020年4月29日下午6:15:54
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @desc 配置 okhttp 与连接池,ConnectionPool 默认创建5个线程，保持5分钟长连接
 */
package com.leesky.ezframework.feign;

import feign.Feign;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

	@Autowired
	private OkHttpLogInterceptor interceptor;

	@Bean
	public okhttp3.OkHttpClient okHttpClient() {
		return new okhttp3.OkHttpClient.Builder()
				// 设置连接超时
				.connectTimeout(60, TimeUnit.SECONDS)
				// 设置读超时
				.readTimeout(60, TimeUnit.SECONDS)
				// 设置写超时
				.writeTimeout(120, TimeUnit.SECONDS)
				// 是否自动重连
				.retryOnConnectionFailure(true).connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES)).addInterceptor(interceptor)
				// 构建OkHttpClient对象
				.build();
	}

}
