/*
 * @作者: 魏来
 * @日期: 2021/9/2  下午3:49
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 带有关系查询
 */
package com.leesky.ezframework.query;

import com.leesky.ezframework.join.interfaces.one2one.One2One;
import com.leesky.ezframework.join.interfaces.one2one.One2oneHandler;
import com.leesky.ezframework.join.utils.JoinUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QueryWithRelation<T> {
    private final One2oneHandler<T> one2oneHandler;

    public void relationship(T t, Map<String, String> param) {
        List<Field> list = JoinUtil.getAllField(t);

        for (Field f : list) {
            One2One o2o = f.getAnnotation(One2One.class);
            if (ObjectUtils.isNotEmpty(o2o))
                this.one2oneHandler.query(o2o, f, t, param);


        }
    }
}
