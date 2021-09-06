/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.one2one;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class One2oneHandler<T> {

    private Field f;

    private Object entity;

    private String relationField;

//    private final IbaseMapper baseMapper;

    private final SpringContextHolder springContextHolder;

    public One2oneHandler<T> build(Field f, Object entity, String relationField) {

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

    public void save(Object v) {

        try {
            Object obj = this.f.get(entity);

            String mapperBeanName = JoinUtil.buildMapperBeanName(this.f);

            BaseMapper iMapper = (BaseMapper) this.springContextHolder.getBean(mapperBeanName);

            BeanUtils.setProperty(obj, Hump2underline.lineToHump(this.relationField), v);

            iMapper.insert(obj);

        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void query(One2One o2o, Field f, T t, Map<String, String> param) {

        DateConverter converter = new DateConverter();
        converter.setPattern(new String("yyyy-MM-dd"));
        ConvertUtils.register(converter, Date.class);

        Object v = JoinUtil.getValue(t, o2o.joinField());// where 中条件值
        String s = param.get(f.getName() + "_select");// select 内容
        s = StringUtils.equals(null, s) ? "*" : s;

        String mapperBeanName = JoinUtil.buildMapperBeanName(f);
        IbaseMapper iMapper = (IbaseMapper) this.springContextHolder.getBean(mapperBeanName);
        HashMap<String,Object> result = iMapper.one2oneQuery(o2o, v, s);

        try {
            Object a = f.getType().getDeclaredConstructor().newInstance();

            result.forEach((k, v1) -> JoinUtil.setValue(a, k, v1));

            JoinUtil.setValue(t, f.getName(), a);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
