/*
 * @作者: 魏来
 * @日期: 2021/9/18  下午12:13
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.mapper;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <li>描述: AbstractAutoMappper.java 中通用代码
 */
public class CommonCode {

    public static void buildList(List<Serializable> idListDistinct, List<Serializable> columnPropertyValueList) {
        for (int s = 0; s < columnPropertyValueList.size(); s++) {
            boolean isExists = false;
            for (int ss = 0; ss < idListDistinct.size(); ss++) {
                if (columnPropertyValueList.get(s) != null && idListDistinct.get(ss) != null && columnPropertyValueList.get(s).toString().equals(idListDistinct.get(ss).toString())) {
                    isExists = true;
                    break;
                }
            }

            if (columnPropertyValueList.get(s) != null && !isExists) {
                idListDistinct.add(columnPropertyValueList.get(s));
            }
        }
    }


    public static <X> List<Serializable> getSerializable(String inverseRefColumnProperty, List<X> entityXList) {
        List<Serializable> columnPropertyValueList;
        List<Serializable> idList = Lists.newArrayList();

        extracted(inverseRefColumnProperty, entityXList, idList);
        List<Serializable> idListDistinct = Lists.newArrayList();
        if (idList.size() > 0)
            CommonCode.buildList(idListDistinct, idList);

        columnPropertyValueList = idListDistinct;
        return columnPropertyValueList;
    }

    public static <X> void extracted(String inverseRefColumnProperty, List<X> entityXList, List<Serializable> idList) {
        for (X x : entityXList) {
            try {
                Field fieldX = x.getClass().getDeclaredField(inverseRefColumnProperty);
                fieldX.setAccessible(true);
                Serializable id = (Serializable) fieldX.get(x);
                if (id != null && !idList.contains(id)) {
                    idList.add(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
