package com.leesky.ezframework.mybatis.mapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.enums.RelationType;

import lombok.extern.slf4j.Slf4j;

/**
 * AutoMapper for one2one/one2many/many2many
 */
@Slf4j
public class AutoMapper extends AbstractAutoMapper {


    /**
     * <li>平台中的model包命名规范必须是com.leesky.ezframework.**.model</li>
     * <li>平台中的model类 必须包含注解：TableName，否则无法自动映射 o2o,o2m,m2o,m2m关系</li>
     * <li>jar中的 model也能加载</li>
     *
     * @作者: 魏来
     * @日期: 2021/9/17  上午10:44
     **/
    public AutoMapper(String entityPackages) {
        ResourcePatternResolver rr = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(entityPackages) + "/**/*.class";
            Resource[] resources = rr.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(rr);
            for (Resource r : resources) {
                if (r.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(r);
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    TableName annotation = clazz.getAnnotation(TableName.class);
                    if (ObjectUtils.isNotEmpty(annotation))
                        autoMapperBean(clazz.getName());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("读取class失败", e);
        }


    }


    /**
     * an entity auto related
     */
    public <T> T mapperEntity(T t) {
        if (t != null) {
            t = super.manyToOne(t);
            t = super.oneToMany(t);
            t = super.oneToOne(t);
            t = super.manyToMany(t);
        }
        return t;
    }

    /**
     * an entity auto related eager or not
     */
    public <T> void mapperEntity(T t, boolean fetchEager) {
        if (t != null) {
            t = super.manyToOne(t, fetchEager);
            t = super.oneToMany(t, fetchEager);
            t = super.oneToOne(t, fetchEager);
            super.manyToMany(t, fetchEager);
        }
    }

    /**
     * an entity auto related by one of property name
     */
    public <T> void mapperEntity(T t, String propertyName) {
        if (t != null) {
            t = super.manyToOne(t, propertyName);
            t = super.oneToMany(t, propertyName);
            t = super.oneToOne(t, propertyName);
            super.manyToMany(t, propertyName);
        }

    }

    /**
     * an entity list auto related
     */
    public <T> List<T> mapperEntityList(List<T> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list = super.manyToOne(list, null, false);
            list = super.oneToMany(list, null, false);
            list = super.oneToOne(list, null, false);
            list = super.manyToMany(list, null, false);
        }

        return list;
    }

    /**
     * an entity list auto related eager or not
     */
    public <T> List<T> mapperEntityList(List<T> list, boolean fetchEager) {
        if (CollectionUtils.isNotEmpty(list)) {
            list = super.manyToOne(list, null, true);
            list = super.oneToMany(list, null, true);
            list = super.oneToOne(list, null, true);
            list = super.manyToMany(list, null, true);
        }

        return list;
    }

    /**
     * 手动触发属性名称=propertyName 的关联查询
     */
    public <T> List<T> mapperEntityList(List<T> list, String propertyName) {
        if (CollectionUtils.isNotEmpty(list)) {
            list = super.manyToOne(list, propertyName, true);
            list = super.oneToMany(list, propertyName, true);
            list = super.oneToOne(list, propertyName, true);
            list = super.manyToMany(list, propertyName, true);
        }

        return list;
    }

    /**
     * an entity set auto related
     */
    public <T> Set<T> mapperEntitySet(Set<T> set) {
        if (CollectionUtils.isNotEmpty(set)) {
            List<T> list = Lists.newArrayList(set);
            mapperEntityList(list);
        }
        return set;
    }

    /**
     * an entity set auto related eager or not
     */
    public <T> Set<T> mapperEntitySet(Set<T> set, boolean fetchEager) {
        if (CollectionUtils.isNotEmpty(set)) {
            List<T> list = Lists.newArrayList(set);
            mapperEntityList(list, true);
        }
        return set;
    }

    /**
     * an entity set auto related by one of property name
     */
    public <T> Set<T> mapperEntitySet(Set<T> set, String propertyName) {
        if (CollectionUtils.isNotEmpty(set)) {
            List<T> list = Lists.newArrayList(set);
            mapperEntityList(list, propertyName);
        }
        return set;
    }

    /**
     * an entity page auto related
     */
    public <E extends IPage<T>, T> E mapperEntityPage(E page) {
        List<T> list = page.getRecords();
        mapperEntityList(list);

        return page;
    }

    /**
     * an entity page auto related eager or not
     */
    public <E extends IPage<T>, T> void mapperEntityPage(E page, boolean fetchEager) {
        List<T> list = page.getRecords();
        mapperEntityList(list, true);
    }

    /**
     * an entity page auto related by one of property name
     */
    public <E extends IPage<T>, T> void mapperEntityPage(E page, String propertyName) {
        List<T> list = page.getRecords();
        mapperEntityList(list, propertyName);
    }

    /**
     * an entity list auto related
     */
    public <T> Collection<T> mapperEntityCollection(Collection<T> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.getClass() == ArrayList.class)
                list = mapperEntityList((List<T>) list);
            else
                list = mapperEntitySet((Set<T>) list);

        }

        return list;
    }

    /**
     * an entity list auto related eager or not
     */
    public <T> Collection<T> mapperEntityCollection(Collection<T> list, boolean fetchEager) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.getClass() == ArrayList.class)
                list = mapperEntityList((List<T>) list, fetchEager);
            else
                list = mapperEntitySet((Set<T>) list, fetchEager);

        }

        return list;
    }

    /**
     * an entity list auto related by one of property name
     */
    public <T> Collection<T> mapperEntityCollection(Collection<T> list, String propertyName) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.getClass() == ArrayList.class)
                list = mapperEntityList((List<T>) list, propertyName);
            else
                list = mapperEntitySet((Set<T>) list, propertyName);

        }
        return list;
    }

    /**
     * an entity/entity list/entity set/entity page auto related
     */
    @SuppressWarnings("unchecked")
    public <E extends IPage<T>, T> void mapper(Object object) {
        if (object != null) {
            if (object.getClass() == ArrayList.class) {
                mapperEntityList((List<T>) object);
            } else if (object.getClass() == HashSet.class) {
                mapperEntitySet((Set<T>) object);
            } else if (object instanceof IPage) {
                mapperEntityPage((E) object);
            } else {
                mapperEntity(object);
            }
        }
    }

    /**
     * an entity/entity list/entity set/entity page auto related eager or not
     */
    @SuppressWarnings("unchecked")
    public <E extends IPage<T>, T> void mapper(Object object, boolean fetchEager) {
        if (object != null) {
            if (object.getClass() == ArrayList.class) {
                mapperEntityList((List<T>) object, fetchEager);
            } else if (object.getClass() == HashSet.class) {
                mapperEntitySet((Set<T>) object, fetchEager);
            } else if (object instanceof IPage) {
                mapperEntityPage((E) object, fetchEager);
            } else {
                mapperEntity(object, fetchEager);
            }
        }
    }

    /**
     * an entity/entity list/entity set/entity page auto related by one of property name
     */
    @SuppressWarnings("unchecked")
    public <E extends IPage<T>, T> void mapper(Object object, String propertyName) {
        if (object != null) {
            if (object.getClass() == ArrayList.class) {
                mapperEntityList((List<T>) object, propertyName);
            } else if (object.getClass() == HashSet.class) {
                mapperEntitySet((Set<T>) object, propertyName);
            } else if (object instanceof IPage) {
                mapperEntityPage((E) object, propertyName);
            } else {
                mapperEntity(object, propertyName);
            }
        }
    }

    /**
     * initialize one or more lazy property manually with transactional
     */
    @Transactional(readOnly = true)
    public <E extends IPage<T>, T> void initialize(Object object, String... propertyNames) {
        for (String propertyName : propertyNames) {
            mapper(object, propertyName);
        }
    }

    private void autoMapperBean(String className) {

        ArrayList<String> o2oFields = Lists.newArrayList();
        ArrayList<String> o2mFields = Lists.newArrayList();
        ArrayList<String> m2oFields = Lists.newArrayList();
        ArrayList<String> m2mFields = Lists.newArrayList();

        try {
            Class<?> clazz = Class.forName(className);

            Field[] fields = clazz.getDeclaredFields();
            if (ArrayUtils.isNotEmpty(fields)) {
                for (Field field : fields) {
                    OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                    if (oneToOne != null)
                        o2oFields.add(field.getName());

                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                    if (oneToMany != null)
                        o2mFields.add(field.getName());

                    ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                    if (manyToOne != null)
                        m2oFields.add(field.getName());

                    ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                    if (manyToMany != null)
                        m2mFields.add(field.getName());
                }

                if (o2oFields.size() > 0)
                    entityMap.put(clazz.getName() + "." + RelationType.ONETOONE.name(), o2oFields.toArray(new String[]{}));


                if (o2mFields.size() > 0)
                    entityMap.put(clazz.getName() + "." + RelationType.ONETOMANY.name(), o2mFields.toArray(new String[]{}));

                if (m2oFields.size() > 0)
                    entityMap.put(clazz.getName() + "." + RelationType.MANYTOONE.name(), m2oFields.toArray(new String[]{}));

                if (m2mFields.size() > 0)
                    entityMap.put(clazz.getName() + "." + RelationType.MANYTOMANY.name(), m2mFields.toArray(new String[]{}));


            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error in scan entity bean");
        }
    }

}
