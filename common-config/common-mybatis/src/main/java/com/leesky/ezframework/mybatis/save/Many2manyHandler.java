/*
 * @作者: 魏来
 * @日期: 2021年10月6日  上午10:41:20
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.condition.TableIdCondition;
import com.leesky.ezframework.mybatis.mapper.IbaseMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类功能说明：
 * <li>处理实体entity中的many2many关系</li>
 * <li>fields是entity中带有many2many注解的属性集合
 */
@Component

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class Many2manyHandler<T> {


    public void handler(String[] fields, T entity, IbaseMapper ibaseMapper, Map<String, String[]> entityMap) throws Exception {

        List<Object> noKey = Lists.newArrayList();
        Map<Object, Object> haveKey = Maps.newHashMap();
        for (String f : fields) {

            Field field = entity.getClass().getDeclaredField(f);
            field.setAccessible(true);
            Object m2m = field.get(entity);// obj 就是要存储的实体集合（entity中 的 many2many关系实体），一般是set或list集合

            List list = m2m instanceof Set ? Lists.newArrayList((Set) m2m) : (List) m2m;

            // 1、查询
            if (CollectionUtils.isNotEmpty(list)) {
                TableIdCondition tidConditionRef = new TableIdCondition(list.get(0).getClass());
                for (Object e : list) {
                    String v = BeanUtils.getProperty(e, tidConditionRef.getFieldOfTableId().getName());// m2m对象的主键值
                    if (StringUtils.isBlank(v))
                        noKey.add(e);
                    else
                        haveKey.put(v, e);

                }


            }
        }
    }
}
