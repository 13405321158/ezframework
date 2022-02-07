package com.leesky.ezframework.utils;

import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/1 下午5:20
 */

@Configuration
public class Po2DtoUtil {

    @Autowired
    private Mapper mapper;

    private static Po2DtoUtil pd;

    @PostConstruct
    public void init() {
        pd = this;
    }

    /**
     * List 实体类 转换器
     *
     * @param source 原数据
     * @param clz    转换类型
     * @return
     */
    public static <T, S> List<T> convertor(List<S> source, Class<T> clz) {

        Assert.isTrue(null != source, "List<T> is null");
        List<T> map = Lists.newArrayList();
        for (S s : source) {
            if (ObjectUtils.isNotEmpty(s))
                map.add(pd.mapper.map(s, clz));
        }
        return map;
    }

    /**
     * Set 实体类 深度转换器
     *
     * @param source 原数据
     * @param clz    目标对象
     */
    public static <T, S> Set<T> convertor(Set<S> source, Class<T> clz) {
        Assert.isTrue(null != source, "List<T> is null");
        Set<T> set = Sets.newHashSet();
        for (S s : source)
            set.add(pd.mapper.map(s, clz));

        return set;
    }

    /**
     * 实体类 深度转换器
     */
    public static <T, S> T convertor(S source, Class<T> clz) {
        Assert.isTrue(ObjectUtils.isNotEmpty(source), "source is null");
        return pd.mapper.map(source, clz);
    }

    public static void convertor(Object source, Object object) {
        pd.mapper.map(source, object);
    }

    public static <T> void copyConvertor(T source, Object object) {
        pd.mapper.map(source, object);
    }
}
