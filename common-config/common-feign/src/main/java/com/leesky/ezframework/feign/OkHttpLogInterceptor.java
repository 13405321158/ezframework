/*
  @author:weilai
 * @Data:2020年4月29日下午6:15:54
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */
package com.leesky.ezframework.feign;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@RefreshScope
@Configuration
public class OkHttpLogInterceptor implements Interceptor {

	@Value("${feign.okhttp.debug:false}")
	private Boolean moniter;

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {

		Request request = chain.request();

		Response response = chain.proceed(request);

		if (moniter) {

			long t1 = System.nanoTime();
			log.info(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
			long t2 = System.nanoTime();
			ResponseBody responseBody = response.peekBody(1024 * 1024);
			log.info(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d, response.headers()));
		}

		return response;
	}
}
