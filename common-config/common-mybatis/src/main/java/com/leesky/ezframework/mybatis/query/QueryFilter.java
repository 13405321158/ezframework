package com.leesky.ezframework.mybatis.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QueryFilter<T> extends QueryWrapper<T> {

    private static final long serialVersionUID = -7119777000984779256L;

    public ParamModel param;
    public Map<String, Object> p1 = Maps.newHashMap();// 原始参数值
    public Map<String, Object> p2 = Maps.newHashMap();// 原始参数转换为下划线格式
    public List<String> op = Lists.newArrayList();// 原始参数转下划线且带有操作符号
    public Multimap<String, String> join = ArrayListMultimap.create();//实体类中 关系实体属性

    private T t ;

    public QueryFilter() {

    }

    public QueryFilter(ParamModel param) throws NoSuchFieldException {
        this.param = param;
        if (StringUtils.equals(getSqlSelect(), null))
            this.setSelect("*");
        else
            this.setSelect(param.getSelect());

        analyzing(this.param.getQueryStr(), Lists.newArrayList());

        ResolvableType s = ResolvableType.forField(getClass().getDeclaredField("t"));
        if (ObjectUtils.isNotEmpty(s))
            System.out.println("obj.getName() = " + s.getGeneric(0));
//        for (Field f : this.getEntityClass().getDeclaredFields()) {
//
//            OneToOne o2o = f.getAnnotation(OneToOne.class);
//            if (ObjectUtils.isNotEmpty(o2o))
//                join.put("o2o", f.getName());
//
//            ManyToMany m2m = f.getAnnotation(ManyToMany.class);
//            if (ObjectUtils.isNotEmpty(m2m))
//                join.put("m2m", f.getName());
//
//
//            OneToMany o2m = f.getAnnotation(OneToMany.class);
//            if (ObjectUtils.isNotEmpty(o2m))
//                join.put("o2m", f.getName());
//
//            ManyToOne m2o = f.getAnnotation(ManyToOne.class);
//            if (ObjectUtils.isNotEmpty(m2o))
//                join.put("m2o", f.getName());
//
//
//        }
    }

    public QueryFilter(ParamModel param, List<String> remove) {
        this.param = param;
        this.setSelect(param.getSelect());
        analyzing(this.param.getQueryStr(), remove);
    }

    /**
     * @作者: 魏来
     * @日期: 2021/8/18 下午2:05
     * @描述: 带有排序字段
     **/
    public QueryFilter(ParamModel param, String orderField, String descAsc) {
        this.param = param;

        this.setSelect(param.getSelect());

        switch (descAsc) {

            case "asc":
                this.orderByAsc(orderField);
                break;

            case "desc":
                this.orderByDesc(orderField);
                break;

        }

        analyzing(param.getQueryStr(), Lists.newArrayList());

    }


    private void setSelect(String str) {
        if (StringUtils.isNotBlank(str)) {
            List<String> ret = Lists.newArrayList();
            String[] array = StringUtils.split(str, ",");
            Arrays.stream(array).forEach(e -> ret.add(Hump2underline.build(e)));
            this.select(StringUtils.join(ret, ","));
        }

    }

    /**
     * <li>:str 是model的属性，所以要用驼峰转换为 数据表字段
     *
     * @作者: 魏来
     * @日期: 2021/10/21  上午11:36
     **/
    private void analyzing(String str, List<String> remove) {

        if (StringUtils.isNotBlank(str)) {
            Map<String, String> params = JSON.parseObject(str, new TypeReference<>() {
            });

            for (Map.Entry<String, String> map : params.entrySet()) {
                if (remove.contains(map.getKey()))// 如果是需要排除的参数，则跳过，继续下一个循环
                    continue;

                if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {

                    str = map.getKey().replace("Q_", "");
                    String[] array = StringUtils.split(str, "_");
                    getOpera(array, map.getValue());

                }

            }

        }

    }

    private void getOpera(String[] array, Object val) {
        if (array.length > 1) {
            p1.put(array[0], val);
            p2.put(Hump2underline.build(array[0]), val);
            String column = Hump2underline.build(array[0]);

            switch (array[1]) {

                case "EQ":
                    this.eq(column, val);
                    op.add(Hump2underline.build(array[0]) + "='" + val + "'");
                    break;

                case "NE":
                    this.ne(column, val);
                    op.add(Hump2underline.build(array[0]) + " != '" + val + "'");
                    break;

                case "GE":
                    this.ge(column, val);
                    op.add(Hump2underline.build(array[0]) + " >= '" + val + "'");
                    break;

                case "GT":
                    this.gt(column, val);
                    op.add(Hump2underline.build(array[0]) + " > '" + val + "'");
                    break;

                case "LE":
                    this.le(column, val);
                    op.add(Hump2underline.build(array[0]) + " <= '" + val + "'");
                    break;

                case "LT":
                    this.lt(column, val);
                    op.add(Hump2underline.build(array[0]) + " < '" + val + "'");
                    break;

                case "LK":
                    this.like(column, val);
                    op.add(Hump2underline.build(array[0]) + " like '%" + val + "%'");
                    break;

                case "LKLeft":
                    this.likeLeft(column, val);
                    op.add(Hump2underline.build(array[0]) + " like '" + val + "%'");
                    break;

                case "LKRight":
                    this.likeRight(column, val);
                    op.add(Hump2underline.build(array[0]) + " like '%" + val + "'");
                    break;

                case "NK":
                    this.notLike(column, val);
                    op.add(Hump2underline.build(array[0]) + " not like '%" + val + "%'");
                    break;

                case "BW":
                    String[] va = StringUtils.split(val.toString(), "~");
                    this.between(column, va[0], va[1]);
                    op.add(Hump2underline.build(array[0]) + " BETWEEN '" + va[0] + "' to '" + va[1] + "'");
                    break;

                case "IN":
                    this.in(column, (Object) String.valueOf(val).split(","));
                    op.add(Hump2underline.build(array[0]) + " in('" + StringUtils.join(Lists.newArrayList(StringUtils.split((String) val, ",")), "','") + "')");
                    break;

            }

        }

    }


}
