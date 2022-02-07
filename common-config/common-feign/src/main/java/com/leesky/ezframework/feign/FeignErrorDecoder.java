/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午9:02:45
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.feign;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.context.annotation.Configuration;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明：
 * <li></li>
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		Exception exception = null;
		try {
			String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
			exception = new RuntimeException(json);
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return exception;
	}
}
