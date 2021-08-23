/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */

package com.leesky.ezframework.utils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder {

//	private static ApplicationContext applicationContext = null;
	public static ConfigurableApplicationContext applicationContext;
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		SpringContextHolder.applicationContext = applicationContext;
//	}
//
//	public static <T> T getBean(Class<T> type) {
//		assertContextInjected();
//		return applicationContext.getBean(type);
//	}
//
//
//
//	public static Object getBean(String name) {
//		assertContextInjected();
//		return applicationContext.getBean(name);
//
//	}
//	
//	public static void assertContextInjected() {
//		if (applicationContext == null) {
//			throw new RuntimeException("applicationContext未注入");
//		}
//	}

	// 定义一个获取已经实例化bean的方法
	public static <T> T getBean(Class<T> c) {
		return applicationContext.getBean(c);
	}

	// 定义一个获取已经实例化bean的方法
	public static Object getBean(String c) {
		Object object = applicationContext.getBean(c);

		return object;
	}
}
