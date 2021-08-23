/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */
package com.leesky.ezframework.ddl.utils;

import com.leesky.ezframework.ddl.constants.Constants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Properties;

@Component
public class ConfigurationUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private static Properties properties = null;

	@Value("${mybatis-plus.leesky.ddl-auto:none}")
	private String tableAuto;

	@Value("${mybatis-plus.leesky.entity-pack:com.leesky.ezframework.*.entity}")
	private String modelPack;

	@Value("${mybatis-plus.leesky.db-type:mysql}")
	private String databaseType;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ConfigurationUtil.applicationContext = applicationContext;
	}

	/**
	 * 获得spring上下文
	 *
	 * @return ApplicationContext spring上下文
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取bean
	 * 
	 * @param name service注解方式name为小驼峰格式
	 * @return Object bean的实例对象
	 */
	public Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 获取配置文件的值，兼容传统spring项目和springboot项目
	 * 
	 * @param key properties
	 */
	public String getConfig(String key) {
		if (Constants.DATABASE_TYPE_KEY.equals(key) && !Constants.NULL.equals(databaseType)) {
			return databaseType;
		}
		if (Constants.MODEL_PACK_KEY.equals(key) && !Constants.NULL.equals(modelPack)) {
			return modelPack;
		}
		if (Constants.TABLE_AUTO_KEY.equals(key) && !Constants.NULL.equals(tableAuto)) {
			return tableAuto;
		}
		if (properties == null) {
			initProperties();
		}
		Object object = properties.get(key);
		return object == null ? null : (String) object;
	}

	private void initProperties() {

		properties = new Properties();
		try {
			String[] postProcessorNames = applicationContext.getBeanNamesForType(BeanFactoryPostProcessor.class, true, true);
			for (String ppName : postProcessorNames) {
				BeanFactoryPostProcessor beanProcessor = applicationContext.getBean(ppName, BeanFactoryPostProcessor.class);
				if (beanProcessor instanceof PropertyResourceConfigurer) {
					PropertyResourceConfigurer propertyResourceConfigurer = (PropertyResourceConfigurer) beanProcessor;
					Method mergeProperties = PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");
					mergeProperties.setAccessible(true);
					Properties props = (Properties) mergeProperties.invoke(propertyResourceConfigurer);

					// get the method convertProperties
					// in class PropertyResourceConfigurer
					Method convertProperties = PropertyResourceConfigurer.class.getDeclaredMethod("convertProperties", Properties.class);
					// convert properties
					convertProperties.setAccessible(true);
					convertProperties.invoke(propertyResourceConfigurer, props);

					properties.putAll(props);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
