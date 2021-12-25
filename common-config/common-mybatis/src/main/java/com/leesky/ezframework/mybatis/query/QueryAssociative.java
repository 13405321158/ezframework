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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
@Slf4j
@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryAssociative<T> {

    private static String table_Name;
    private final static String SPLIT_DOT = ".";// 点号分隔符
    private final static String SPLIT_COMMA = ",";//逗号分隔符
    private final static String SPLIT_PREFIX = "a.";//别名前缀
    private final static String SPLIT_EQUAL = "=";//等号

    private final static String ON_A = "ON a.";
    private final static String LEFT_JOIN = "LEFT JOIN";


    /**
     * <li>构造多表联合查询时的select内容,原始字符串x1,x2,b.asDb</li>
     * <li>目的字符串：a.x1,a.x2,b.as_db</li>
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午4:20
     **/
    public String makeSelect(String s) {
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
     * 构建left join
     *
     * @author： 魏来
     * @date: 2021/12/25 下午2:13
     */
    public void makeLeftJoin(QueryFilter<T> filter, Class<T> entity) {
        filter.setClz(entity);
        filter.setTableName(entity.getAnnotation(TableName.class).value() + " a");

        if (StringUtils.isBlank(filter.getSqlSelect()))
            filter.select("a.*");
        else
            filter.select(makeSelect(filter.getSqlSelect()));

        analyticalRelation(filter);
    }

    /**
     * 解析映射关系
     *
     * @author： 魏来
     * @date: 2021/12/25 下午1:40
     */

    private void analyticalRelation(QueryFilter<T> filter) {

        if (ObjectUtils.isNotEmpty(filter.getClz())) {

            Set<String> include = Sets.newHashSet();
            Consumer consumer = (e) -> include.add(StringUtils.substringBefore((String) e, SPLIT_DOT));
            filter.p1.keySet().forEach(consumer);//判断where条件中是否需要 left jon
            Arrays.stream(StringUtils.split(filter.getSqlSelect(), SPLIT_COMMA)).forEach(consumer);//判断 select 中内容 是否需要left join

            for (Field f : filter.getClz().getDeclaredFields()) {

                OneToOne o2o = f.getAnnotation(OneToOne.class);
                if (ObjectUtils.isNotEmpty(o2o) && include.contains(f.getName()))
                    filter.join.add(buildJoinLeft(f, "OneToOne"));

                OneToMany o2m = f.getAnnotation(OneToMany.class);
                if (ObjectUtils.isNotEmpty(o2m) && include.contains(f.getName()))
                    filter.join.add(buildJoinLeft(f, "OneToMany"));

                ManyToOne m2o = f.getAnnotation(ManyToOne.class);
                if (ObjectUtils.isNotEmpty(m2o) && include.contains(f.getName()))
                    filter.join.add(buildJoinLeft(f, "ManyToOne"));

                ManyToMany m2m = f.getAnnotation(ManyToMany.class);
                if (ObjectUtils.isNotEmpty(m2m) && include.contains(f.getName()))
                    filter.join.add(buildJoinLeft_M2M(f));
            }
        }
    }


    /**
     * <li>构造left join条件:适用于： o2o\o2m\m2o
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午5:32
     **/
    private String buildJoinLeft(Field f, String aName) {
        List<String> list = Lists.newArrayList();
        try {
            list.add(LEFT_JOIN);
            list.add(f.getAnnotation(EntityMapper.class).entityClass().getAnnotation(TableName.class).value());
            list.add(f.getName());
            list.add(ON_A + f.getAnnotation(JoinColumn.class).name());
            list.add(SPLIT_EQUAL);
            list.add(f.getName() + SPLIT_DOT + f.getAnnotation(JoinColumn.class).referencedColumnName());
        } catch (Exception e) {
            log.error("构建Join left时出错：数据表名→" + table_Name + "对应字段→" + f.getName() + ";注解类型：" + aName);
        }

        return StringUtils.join(list, " ");
    }

    /**
     * <li>构造left join条件:适用于： m2m
     *
     * @作者: 魏来
     * @日期: 2021/10/28  下午12:11
     **/
    private String buildJoinLeft_M2M(Field f) {
        List<String> list = Lists.newArrayList();
        try {
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
        } catch (Exception e) {
            log.error("构建Join left时出错：数据表名→" + table_Name + "对应字段→" + f.getName() + ";注解类型：Many2Many");
        }


        return StringUtils.join(list, " ");
    }

    /**
     * <li>: List<clz> 或者Set<clz> 中的 clz所对应的 @TableName之 表名称
     *
     * @作者: 魏来
     * @日期: 2021/10/28  下午12:00
     **/
    private String getCollectType(Field f) {
        ParameterizedType pt = (ParameterizedType) f.getGenericType();
        Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
        return clz.getAnnotation(TableName.class).value() + " " + f.getName();
    }
}
