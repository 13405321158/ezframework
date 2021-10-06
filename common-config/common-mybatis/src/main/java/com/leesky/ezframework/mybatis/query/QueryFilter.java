package com.leesky.ezframework.mybatis.query;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Hump2underline;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QueryFilter<T> extends QueryWrapper<T> {

    private static final long serialVersionUID = -1998448102575919460L;

    public Map<String, Object> p1 = Maps.newHashMap();//原始参数值
    public Map<String, Object> p2 = Maps.newHashMap();//原始参数转换为驼峰格式

    private ParamModel param;

    public QueryFilter() {
    }

    public QueryFilter(ParamModel param) {
        this.param = param;
        setSelect(param.getExt());
        analyzing01(param.getQueryStr(), Lists.newArrayList());
    }

    /**
     * @作者: 魏来
     * @日期: 2021/8/18  下午2:01
     * @描述: 适用数据表字段格式如：ID_, name_; 即字段含有下划线
     **/
    public QueryFilter(ParamModel param, Boolean flag) {
        this.param = param;
        setSelect(param.getExt());
        analyzing02(param.getQueryStr());
    }

    public QueryFilter(ParamModel param, List<String> remove) {

        this.param = param;
        setSelect(param.getExt());
        analyzing01(param.getQueryStr(), remove);

    }

    /**
     * @作者: 魏来
     * @日期: 2021/8/18  下午2:05
     * @描述: 带有排序字段
     **/
    public QueryFilter(ParamModel param, String orderField, String descAsc) {

        this.param = param;
        setSelect(param.getExt());

        switch (descAsc) {

            case "asc":
                this.orderByAsc(orderField);
                break;

            case "desc":
                this.orderByDesc(orderField);
                break;

        }

        analyzing01(param.getQueryStr(), Lists.newArrayList());

    }


    public IPage<T> page() {
        return new Page<>(param.getPage(), param.getLimit());
    }


    private void setSelect(String str) {

        if (StringUtils.isNotBlank(str)) {

            List<String> ret = Lists.newArrayList();

            String[] array = StringUtils.split(str, ",");
            for (String c : array) // @Select 内容是Model属性，所以用驼峰命名规则转为：数据表字段。当然如果你在@Select直接用数据表字段更好
                ret.add(Hump2underline.build(c));

            this.select(ret.toArray(new String[0]));

        }

    }


    private void analyzing01(String str, List<String> remove) {

        if (StringUtils.isNotBlank(str)) {
            Map<String, String> params = JSON.parseObject(str, new TypeReference<>() {
            });


            for (Map.Entry<String, String> map : params.entrySet()) {
                if (remove.contains(map.getKey()))//如果是需要排除的参数，则跳过，继续下一个循环
                    continue;

                if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {

                    str = map.getKey().replace("Q_", "");
                    String[] array = StringUtils.split(str, "_");
                    getOpera(array, map.getValue(), true);

                }

            }

        }

    }


    private void analyzing02(String str) {

        if (StringUtils.isNotBlank(str)) {

            Map<String, String> params = JSON.parseObject(str, new TypeReference<>() {
            });
            for (Map.Entry<String, String> map : params.entrySet()) {

                if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {

                    str = map.getKey();
                    String[] array = StringUtils.split(str, "|");
                    getOpera(array, map.getValue(), false);

                }

            }

        }

    }


    private void getOpera(String[] array, Object val, Boolean v) {

        String column;

        if (array.length > 1) {
            p1.put(array[0], val);
            p2.put(Hump2underline.build(array[0]), val);

            if (v)
                column = Hump2underline.build(array[0]);
            else
                column = array[0];

            switch (array[1]) {

                case "EQ":
                    this.eq(column, val);
                    break;

                case "NE":
                    this.ne(column, val);
                    break;

                case "GE":
                    this.ge(column, val);
                    break;

                case "GT":
                    this.gt(column, val);
                    break;

                case "LE":
                    this.le(column, val);
                    break;

                case "LT":
                    this.lt(column, val);
                    break;

                case "LK":
                    this.like(column, val);
                    break;

                case "NK":
                    this.notLike(column, val);
                    break;

                case "BW":
                    String[] va = StringUtils.split(val.toString(), "~");
                    this.between(column, va[0], va[1]);
                    break;

                case "[BW]":// 查询条件是日期，并且格式如：2020-03-01~2021-03-15 ,数据库格式 年月日时分秒，为了包括最后一天数据
                    String[] va1 = StringUtils.split(val.toString(), "~");
                    this.between(column, va1[0], va1[1] + " 23:59:59");
                    break;

                case "IN":
                    this.in(column, (Object) String.valueOf(val).split(","));
                    break;

            }

        }

    }


}
