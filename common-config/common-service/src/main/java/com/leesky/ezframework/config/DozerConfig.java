package com.leesky.ezframework.config;

import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/1 下午6:09
 */
@Configuration
public class DozerConfig {

    @Value("${dozer.mapping-files:classpath:/dozer/*Dozer.xml}")
    private Resource[] resources;

    @Bean
    public DozerBeanMapperFactoryBean dozerMapper() throws IOException {
        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        if (ArrayUtils.isNotEmpty(resources))
            dozerBeanMapperFactoryBean.setMappingFiles(resources);
        return dozerBeanMapperFactoryBean;
    }
}