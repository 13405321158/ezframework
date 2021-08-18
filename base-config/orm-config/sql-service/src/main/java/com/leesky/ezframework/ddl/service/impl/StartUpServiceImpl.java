package com.leesky.ezframework.ddl.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Sets;
import com.leesky.ezframework.ddl.constants.Constants;
import com.leesky.ezframework.ddl.service.ImysqlCreateTableService;
import com.leesky.ezframework.ddl.service.IstartUpService;
import com.leesky.ezframework.ddl.utils.ConfigurationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;

/**
 * @author: weilai
 * @Data:上午9:59:22,2020年2月2日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>启动时进行处理的实现类
 */

@Slf4j
@Service
public class StartUpServiceImpl implements IstartUpService {


    /**
     * 数据库类型：mysql
     */
    public static String MYSQL = "mysql";


    private final ImysqlCreateTableService service;

    private final ConfigurationUtil springContextUtil;


    @Autowired
    public StartUpServiceImpl(ConfigurationUtil s, ImysqlCreateTableService service) {
        this.service = service;
        this.springContextUtil = s;
    }


    @PostConstruct
    public void startHandler() {
        String tableAuto = springContextUtil.getConfig(Constants.TABLE_AUTO_KEY);

        if (StringUtils.equals("update", tableAuto)) {
            log.info("mybatis-plus.leesky.ddl-auto=update[新建或更新表结构]");
            String packName = findAll();
            this.service.createMysqlTable(packName);
        }
    }

    /**
     * @ver: 1.0.0
     * @author: weilai
     * @data:下午3:51:06,2020年2月3日
     * @desc: <li>查找全部带有 @TableName 注解的类
     */

    private String findAll() {
        HashSet<String> packName = Sets.newHashSet();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String RESOURCE_PATTERN = "/**/*.class";
        String BASE_PACKAGE = "com.leesky.ezframework.**.model";

        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    TableName annotation = clazz.getAnnotation(TableName.class);
                    if (annotation != null)
                        packName.add(clazz.getPackage().getName());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("读取class失败", e);
        }

        return StringUtils.join(packName, ";");
    }
}
