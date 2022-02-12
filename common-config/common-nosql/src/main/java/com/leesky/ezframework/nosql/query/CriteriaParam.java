/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.nosql.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.leesky.ezframework.query.ParamModel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Date;
import java.util.Map;

@NoArgsConstructor
public class CriteriaParam {

    private ParamModel model = null;

    public Criteria make() {
        Map<String, Object> cr = getCriteria(model);
        return buildCriteria(cr);
    }


    private Map<String, Object> getCriteria(ParamModel model) {
        Map<String, Object> map = Maps.newHashMap();

        if (model.getQueryStr() != null) {

            Map<String, String> params = JSON.parseObject(model.getQueryStr().replace("Q_", ""), new TypeReference<>() {
            });

            for (Map.Entry<String, String> param : params.entrySet()) {
                if (StringUtils.isNotBlank(param.getValue()))
                    map.put(param.getKey(), param.getValue());
            }
        }
        return map;
    }

    private Criteria buildCriteria(Map<String, Object> map) {
        Criteria criteria = new Criteria();
        for (Map.Entry<String, Object> m : map.entrySet())
            getOpera(m, criteria);

        return criteria;
    }

    private void getOpera(Map.Entry<String, Object> map, Criteria criteria) {

        String key = map.getKey();
        String[] array = StringUtils.split(key, "_");
        if (array.length == 2) // 常规查询
            cr01(criteria, array, map.getValue());
        else if (array.length == 3) // 日期类型查询
            cr02(criteria, array, map.getValue());

    }



    public CriteriaParam(ParamModel model) {
        this.model = model;
    }

    /**
     * @作者: 魏来
     * @日期: 2021/8/18  下午4:58
     * @描述: Q_xxx_xx_D
     **/
    private void cr02(Criteria criteria, String[] array, Object value) {
        try {
            Date start_date = DateUtils.parseDate(StringUtils.substring((String) value, 0, 19), "yyyy-MM-dd HH:mm:ss");
            Date end_date = DateUtils.parseDate(StringUtils.substring((String) value, 22, 41), "yyyy-MM-dd HH:mm:ss");
            criteria.andOperator(Criteria.where(array[0]).gte(start_date), Criteria.where(array[0]).lte(end_date));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @作者: 魏来
     * @日期: 2021/8/18  下午4:58
     * @描述: 前台参数格式：Q_参数_XX；
     **/
    private void cr01(Criteria criteria, String[] array, Object value) {

        switch (array[1]) {
            case "EQ":
                criteria.and(array[0]).is(value);
                break;
            case "NE":
                criteria.and(array[0]).ne(value);
                break;
            case "GT":
                criteria.and(array[0]).gt(value);
                break;
            case "GTE":
                criteria.and(array[0]).gte("3");
                break;
            case "LT":
                criteria.and(array[0]).lt(value);
                break;
            case "LTE":
                criteria.and(array[0]).lte(value);
                break;
            case "LK":
                criteria.and(array[0]).regex(".*" + value + ".*");
                break;
        }

    }
}
