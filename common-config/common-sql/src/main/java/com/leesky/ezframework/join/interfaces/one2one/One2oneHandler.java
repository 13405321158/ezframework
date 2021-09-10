/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.one2one;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class One2oneHandler<T> {

    private Field f;

    private Object entity;

    private String relationField;

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
            if (ObjectUtils.isNotEmpty(obj)) {
                String mapperBeanName = JoinUtil.buildMapperBeanName(this.f);
                BaseMapper iMapper = (BaseMapper) this.springContextHolder.getBean(mapperBeanName);
                BeanUtils.setProperty(obj, Hump2underline.lineToHump(this.relationField), v);
                iMapper.insert(obj);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    public void query(One2One o2o, Field f, T t, Map<String, String> param) {
        registerCover();
        Object v = JoinUtil.getValue(t, o2o.joinField());// where 中条件值
        if (ObjectUtils.isNotEmpty(v)) {
            String s = param.get(f.getName() + "_select");// select 内容
            s = StringUtils.equals(null, s) ? "*" : s;

            String mapperBeanName = JoinUtil.buildMapperBeanName(f);
            IbaseMapper iMapper = (IbaseMapper) this.springContextHolder.getBean(mapperBeanName);
            HashMap<String, Object> result = iMapper.one2oneQuery(o2o, v, s);

            try {
                Object a = f.getType().getDeclaredConstructor().newInstance();
                Map<String, Object> value = getColumnValue(a, result);
                BeanUtils.populate(a, value);
                JoinUtil.setValue(t, f.getName(), a);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @作者: 魏来
     * @日期: 2021/9/8  上午11:10
     * @描述: 参数target是一个实体类, 参数data
     **/
    private Map<String, Object> getColumnValue(Object target, HashMap<String, Object> data) {
        String column;
        Map<String, Object> map = Maps.newHashMap();
        if (MapUtils.isNotEmpty(data)) {
            List<Field> fields = JoinUtil.getAllField(target);
            for (Field fs : fields) {
                TableField tableField = fs.getAnnotation(TableField.class);//model属性上是否有 TableField注解
                if (ObjectUtils.isNotEmpty(tableField))
                    column = tableField.value();// 有注解则取注解的名称
                else
                    column = Hump2underline.build(fs.getName());// 没有注解则取属性的驼峰命名值

                if (ObjectUtils.isNotEmpty(data.get(column)))
                    map.put(fs.getName(), data.get(column));
            }
        }
        return map;
    }


    //注册一个日期转换器:
    // BeanUtils.populate 这个方法会遍历map<key,value>中的key,如果bean中有这个属性，就把这个key对应的value值赋给bean的属性。
    // BeanUtils.populate 的getProperty,setProperty方法其实都会调用convert进行转换
    //但Converter只支持一些基本的类型，甚至连Java.util.Date类型也不支持。而且它比较笨的一个地方是当遇到不认识的类型时，居然会抛出异常来。
    //所以这里注册一个转换器
    private void registerCover() {
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                Date ret = null;
                try {
                    if (value instanceof String)
                        ret = DateUtils.parseDate((String) value, "yyyy-MM-dd HH:mm:ss");

                } catch (Exception ignored) {
                }
                return ret;
            }

        }, Date.class);
    }
}
