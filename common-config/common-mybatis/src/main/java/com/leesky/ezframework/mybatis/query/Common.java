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
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <li>描述: 多表联合查询时拼接 sql语句
 */
public class Common {

    public static <T> void joinQueryStr(Class<T> clz, QueryFilter<T> slf, List<String> join) {

        if (ObjectUtils.isNotEmpty(clz)) {
            List<String> include = Lists.newArrayList();

            slf.p1.keySet().forEach(e -> include.add(StringUtils.substringBefore(e, ".")));


            for (Field f : clz.getDeclaredFields()) {

                OneToOne o2o = f.getAnnotation(OneToOne.class);
                if (ObjectUtils.isNotEmpty(o2o) && include.contains(f.getName()))
                    join.add(build2One(f));

//                ManyToMany m2m = f.getAnnotation(ManyToMany.class);
//                if (ObjectUtils.isNotEmpty(m2m))
//                    join.put("m2m", f.getName());
//
//
//                OneToMany o2m = f.getAnnotation(OneToMany.class);
//                if (ObjectUtils.isNotEmpty(o2m))
//                    join.put("o2m", f.getName());
//
                ManyToOne m2o = f.getAnnotation(ManyToOne.class);
                if (ObjectUtils.isNotEmpty(m2o) && include.contains(f.getName()))
                    join.add(build2One(f));


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
        String[] a = StringUtils.split(s, ",");
        for (String str : a) {
            String[] b = StringUtils.split(str, ".");
            if (b.length == 1)
                list.add("a." + str);
            else list.add(str);
        }
        return StringUtils.join(list, ",");
    }

    /**
     * <li>构造left join条件: 适合实体中带有one2one 和  many2one
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午5:32
     **/

    private static String build2One(Field f) {
        List<String> list = Lists.newArrayList();
        list.add("LEFT JOIN");
        list.add(f.getAnnotation(EntityMapper.class).entityClass().getAnnotation(TableName.class).value());
        list.add(f.getName());
        list.add("ON a." + f.getAnnotation(JoinColumn.class).name());
        list.add("=");
        list.add(f.getName() + "." + f.getAnnotation(JoinColumn.class).referencedColumnName());
        return StringUtils.join(list, " ");
    }


}
