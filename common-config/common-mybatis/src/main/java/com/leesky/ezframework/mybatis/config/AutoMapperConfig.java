package com.leesky.ezframework.mybatis.config;

import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <li>:初始化AutoMapper，注意：
 * <li>平台中的model包命名规范必须是com.leesky.ezframework.**.model
 * <li>平台中的model类 必须包含注解：TableName，否则无法自动映射 o2o,o2m,m2o,m2m关系</li>
 *
 * @作者: 魏来
 * @日期: 2021/9/17  上午10:40
 **/

@Configuration
public class AutoMapperConfig {
    @Bean
    public AutoMapper autoMapper() {
        return new AutoMapper("com.leesky.ezframework.**.model");
    }

}
