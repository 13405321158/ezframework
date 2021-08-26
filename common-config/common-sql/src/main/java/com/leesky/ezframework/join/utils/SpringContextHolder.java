/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */

package com.leesky.ezframework.join.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> type) {
		assertContextInjected();
		return applicationContext.getBean(type);
	}



	public static Object getBean(String name) {
		assertContextInjected();

		return applicationContext.getBean(name);

	}
	
	public static void assertContextInjected() {
		if (applicationContext == null) {
			throw new RuntimeException("applicationContext未注入");
		}
	}


}
