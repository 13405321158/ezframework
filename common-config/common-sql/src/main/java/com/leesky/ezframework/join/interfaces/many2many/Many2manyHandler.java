/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.many2many;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.service.IbaseService;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Component
@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
public class Many2manyHandler<T> {

    private Field f;

    private Object key;

    private Object entity;

    private Many2Many m2m;

    private Many2manyDTO dto;

    @Autowired
    private SpringContextHolder springContextHolder;

    public Many2manyHandler() {
    }

    public Many2manyHandler build(Many2manyDTO dto) {
        this.dto = dto;
        return this;
    }

    public Many2manyHandler build(Field f, Object entity, Many2Many m2m) {
        this.f = f;
        this.m2m = m2m;
        this.entity = entity;
        f.setAccessible(true);
        return this;
    }

    public Multimap save() {// 存储 另外一方 many 实体
        Multimap multimap = ArrayListMultimap.create();

        try {
            Object obj = this.f.get(entity);
            if (ObjectUtils.isNotEmpty(obj)) {
                String serviceBeanName = JoinUtil.buildServiceBeanNaem(f);
                IbaseService service = (IbaseService) this.springContextHolder.getBean(serviceBeanName);

                for (Object o : (Set) obj) {// 如果使用批量插入，则报错，以后优化吧
                    Object id = JoinUtil.getId(o);

                    if (ObjectUtils.isEmpty(id)) {
                        service.insert(o, false);
                    } else {
                        QueryFilter f = new QueryFilter<>();
                        f.select("id");
                        f.eq("id", id.toString());
                        Object dd = service.findOne(f);

                        if (ObjectUtils.isEmpty(dd))
                            service.insert(o, false);
                        else
                            multimap.put("exist", id.toString());
                    }
                    // 主键关联，返回子表主键; 非主键关联，返回指定的关联字段值
                    Object shipDate = StringUtils.equals("id", m2m.joinColumn()) ? JoinUtil.getId(o) : JoinUtil.getValue(o, m2m.joinColumn());

                    multimap.put("ids", shipDate);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return multimap;
    }

    public void save(Object v) {// 存储中间表
        dto.build(v);
        IbaseMapper baseMapper = (IbaseMapper) this.springContextHolder.getBean("ibaseMapper");
//        // 1、首先在中间表中删除
//        if (StringUtils.isNotBlank(dto.getTargetValue()))
//            baseMapper.delM2M(dto);
//        // 2、然后插入
        baseMapper.insertM2M(dto);
    }

    public void query(Many2Many m2m, Field f, T t, Map<String, String> param, IbaseMapper ibaseMapper) {
        Object v = JoinUtil.getValue(t, m2m.joinColumn());// 在中间表查询的 where 中条件值

        String s = param.get(f.getName() + "_select");// select 内容
        s = StringUtils.equals(null, s) ? "a.*" : ("a." + String.join(",", s)).replace(",", ",a.");

        List data = ibaseMapper.many2manyQuery(m2m, v, s);

        if (CollectionUtils.isNotEmpty(data))
            JoinUtil.setValue(t, f.getName(), Sets.newHashSet(data));
    }
}
