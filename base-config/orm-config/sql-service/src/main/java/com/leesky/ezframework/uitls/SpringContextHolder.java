/**
 * @author:weilai
 * @Data:2021年2月19日下午3:17:59
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>
 */

package com.leesky.ezframework.uitls;

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
