package com.leesky.ezframework.mybatis.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class QueryFilter<T> extends QueryWrapper<T> {

    private static final long serialVersionUID = -7119777000984779256L;

    private Class<T> clz;
    private String tableName;//表名称,即clz的表名称
    private ParamModel param;//查询参数对象
    private List<String> op = Lists.newArrayList();// where原始参数转下划线且带有操作符号
    public List<String> join = Lists.newArrayList();//left join 字符串
    public Map<String, Object> p1 = Maps.newHashMap();// where原始参数值
    private Map<String, Object> p2 = Maps.newHashMap();// where原始参数转换为下划线格式
    private List<String> remove = Lists.newArrayList();//需要排除的条件


    /**
     * <li>:单表查询用、noSQL查询用
     *
     * @作者: 魏来
     * @日期: 2021/10/27  下午2:36
     **/
    public QueryFilter(ParamModel param) {
        this.param = param;
        if (StringUtils.isNotBlank(param.getSelect()))
            this.select(this.param.getSelect());
        analyzing(this.param.getQueryStr());
    }

    /**
     * @作者: 魏来
     * @日期: 2021/10/27  下午2:35
     **/
    public QueryFilter(ImmutableMap<String, String> map) {
        this.param = new ParamModel(map);
        analyzing(this.param.getQueryStr());
    }


    /**
     * <li>:str 是model的属性，所以要用驼峰转换为 数据表字段
     *
     * @作者: 魏来
     * @日期: 2021/10/21  上午11:36
     **/
    private void analyzing(String str) {

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

            //如果array[0]中还有"."例如laoPo.createDate,则要把 createDate 转换为驼峰
            String[] a = StringUtils.split(array[0], ".");
            if (a.length > 1)
                array[0] = a[0] + "." + Hump2underline.build(a[1]);
            else
                array[0] = "a." + array[0];

            switch (array[1]) {

                case "EQ":
                    this.eq(column, val);
                    op.add(array[0] + "='" + val + "'");
                    break;

                case "NE":
                    this.ne(column, val);
                    op.add(array[0] + " != '" + val + "'");
                    break;

                case "GE":
                    this.ge(column, val);
                    op.add(array[0] + " >= '" + val + "'");
                    break;

                case "GT":
                    this.gt(column, val);
                    op.add(array[0] + " > '" + val + "'");
                    break;

                case "LE":
                    this.le(column, val);
                    op.add(array[0] + " <= '" + val + "'");
                    break;

                case "LT":
                    this.lt(column, val);
                    op.add(array[0] + " < '" + val + "'");
                    break;

                case "LK":
                    this.like(column, val);
                    op.add(array[0] + " like '%" + val + "%'");
                    break;

                case "LKLeft":
                    this.likeLeft(column, val);
                    op.add(array[0] + " like '" + val + "%'");
                    break;

                case "LKRight":
                    this.likeRight(column, val);
                    op.add(array[0] + " like '%" + val + "'");
                    break;

                case "NK":
                    this.notLike(column, val);
                    op.add(array[0] + " not like '%" + val + "%'");
                    break;

                case "BW":
                    String[] va = StringUtils.split(val.toString(), "~");
                    this.between(column, va[0], va[1]);
                    op.add(array[0] + " BETWEEN '" + va[0] + "' to '" + va[1] + "'");
                    break;

                case "IN":
                    this.in(column, (Object) String.valueOf(val).split(","));
                    op.add(array[0] + " in('" + StringUtils.join(Lists.newArrayList(StringUtils.split((String) val, ",")), "','") + "')");
                    break;

            }

        }

    }


}
