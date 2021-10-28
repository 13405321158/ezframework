/*
 * @作者: 魏来
 * @日期: 2021/10/27  下午2:15
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.query;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.utils.Hump2underline;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <li>描述: 多表联合查询时拼接 sql语句
 * <li>描述：约定大于实现：命名必须采用驼峰方式</li>
 */
public class Common {

    private final static String SPLIT_DOT = ".";// 点号分隔符
    private final static String SPLIT_COMMA = ",";//逗号分隔符
    private final static String SPLIT_PREFIX = "a.";//别名前缀
    private final static String SPLIT_EQUAL = "=";//等号
    private final static String LEFT_JOIN = "LEFT JOIN";
    private final static String ON_A = "ON a.";

    public static <T> void joinQueryStr(Class<T> clz, QueryFilter<T> slf, List<String> join) {

        if (ObjectUtils.isNotEmpty(clz)) {

            Set<String> include = Sets.newHashSet();
            Consumer consumer = (e) -> include.add(StringUtils.substringBefore((String) e, SPLIT_DOT));
            slf.p1.keySet().forEach(consumer);//判断where条件中是否需要 left jon
            Arrays.stream(StringUtils.split(slf.getSqlSelect(), SPLIT_COMMA)).forEach(consumer);//判断 select 中内容 是否需要left join

            for (Field f : clz.getDeclaredFields()) {

                OneToOne o2o = f.getAnnotation(OneToOne.class);
                if (ObjectUtils.isNotEmpty(o2o) && include.contains(f.getName()))
                    join.add(buildJoinLeft(f));

                OneToMany o2m = f.getAnnotation(OneToMany.class);
                if (ObjectUtils.isNotEmpty(o2m) && include.contains(f.getName()))
                    join.add(buildJoinLeft(f));

                ManyToOne m2o = f.getAnnotation(ManyToOne.class);
                if (ObjectUtils.isNotEmpty(m2o) && include.contains(f.getName()))
                    join.add(buildJoinLeft(f));

                ManyToMany m2m = f.getAnnotation(ManyToMany.class);
                if (ObjectUtils.isNotEmpty(m2m) && include.contains(f.getName()))
                    join.add(buildJoinLeft_M2M(f));
            }
        }
    }

    /**
     * <li>构造多表联合查询时的select内容: select a.x1,a.x2</li>
     * <li>如上,向x1,x2 前增加"a."</li>
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午4:20
     **/
    public static String buildSelect(String s) {
        List<String> list = Lists.newArrayList();
        String[] a = StringUtils.split(s, SPLIT_COMMA);
        for (String str : a) {
            String[] b = StringUtils.split(str, SPLIT_DOT);
            if (b.length == 1)
                list.add(SPLIT_PREFIX + Hump2underline.build(str));
            else
                list.add(b[0] + SPLIT_DOT + Hump2underline.build(b[1]));
        }
        return StringUtils.join(list, SPLIT_COMMA);
    }

    /**
     * <li>构造left join条件:适用于： o2o\o2m\m2o
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午5:32
     **/
    private static String buildJoinLeft(Field f) {
        List<String> list = Lists.newArrayList();
        list.add(LEFT_JOIN);
        list.add(f.getAnnotation(EntityMapper.class).entityClass().getAnnotation(TableName.class).value());
        list.add(f.getName());
        list.add(ON_A + f.getAnnotation(JoinColumn.class).name());
        list.add(SPLIT_EQUAL);
        list.add(f.getName() + SPLIT_DOT + f.getAnnotation(JoinColumn.class).referencedColumnName());
        return StringUtils.join(list, " ");
    }

    /**
     * <li>构造left join条件:适用于： m2m
     *
     * @作者: 魏来
     * @日期: 2021/10/28  下午12:11
     **/
    private static String buildJoinLeft_M2M(Field f) {
        List<String> list = Lists.newArrayList();
        list.add(LEFT_JOIN);
        list.add(f.getAnnotation(EntityMapper.class).entityClass().getAnnotation(TableName.class).value());
        list.add("AS m");
        list.add(ON_A + f.getAnnotation(JoinColumn.class).name());
        list.add(SPLIT_EQUAL);
        list.add("m." + f.getAnnotation(JoinColumn.class).referencedColumnName());
        list.add(LEFT_JOIN);
        list.add(getCollectType(f));
        list.add("ON " + f.getName() + SPLIT_DOT + f.getAnnotation(InverseJoinColumn.class).name());
        list.add(SPLIT_EQUAL);
        list.add("m." + f.getAnnotation(InverseJoinColumn.class).referencedColumnName());

        return StringUtils.join(list, " ");
    }

    /**
     * <li>: List<clz> 或者Set<clz> 中的 clz所对应的 @TableName之 表名称
     *
     * @作者: 魏来
     * @日期: 2021/10/28  下午12:00
     **/
    private static String getCollectType(Field f) {
        ParameterizedType pt = (ParameterizedType) f.getGenericType();
        Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
        return clz.getAnnotation(TableName.class).value() + " " + f.getName();
    }
}
