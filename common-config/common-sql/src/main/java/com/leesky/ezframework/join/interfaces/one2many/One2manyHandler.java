/*
 * @作者: 魏来
 * @日期: 2021/8/27  上午11:22
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.one2many;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Data
@Component
public class One2manyHandler {

    private Field f;

    private Object entity;

    private String relationField;

    @Autowired
    private SpringContextHolder springContextHolder;

    public One2manyHandler build(Field f, Object entity, String relationField) {

        this.f = f;
        this.entity = entity;
        this.relationField = relationField;

        f.setAccessible(true);

        return this;
    }


    public Object save() {
        Object obj = null;
        try {
            obj = this.f.get(entity);

            String mapperBeanName = JoinUtil.buildMapperBeanName(this.f);

            BaseMapper iMapper = (BaseMapper) this.springContextHolder.getBean(mapperBeanName);

            iMapper.insert(obj);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public Object save(Object v) {
        Object obj = null;
        try {
            obj = this.f.get(entity);

            String mapperBeanName = JoinUtil.buildMapperBeanName(this.f);

            BaseMapper iMapper = (BaseMapper) this.springContextHolder.getBean(mapperBeanName);

            BeanUtils.setProperty(obj, Hump2underline.lineToHump(this.relationField), v);

            iMapper.insert(obj);

        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
