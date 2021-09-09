/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:37:30
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.interfaces.many2many;

import com.google.common.collect.Lists;
import com.leesky.ezframework.join.mapper.IbaseMapper;
import com.leesky.ezframework.join.utils.JoinUtil;
import com.leesky.ezframework.join.utils.SpringContextHolder;
import com.leesky.ezframework.query.QueryFilter;
import com.leesky.ezframework.service.IbaseService;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Data
@Component
@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
public class Many2manyHandler {

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

    public List<Object> save() {// 存储 另外一方 many 实体
        List<Object> ret = Lists.newArrayList();
        try {
            Object obj = this.f.get(entity);
            if (ObjectUtils.isNotEmpty(obj)) {
                String serviceBeanName = JoinUtil.buildServiceBeanNaem(f);
                IbaseService service = (IbaseService) this.springContextHolder.getBean(serviceBeanName);

                for (Object o : (Set) obj) {// 如果使用批量插入，则报错，以后优化吧
                    Object id = JoinUtil.getId(o);

                    if (ObjectUtils.isNotEmpty(id)) {
                        QueryFilter filter = new QueryFilter<>();
                        filter.select("id");
                        filter.eq("id", id.toString());
                        Object dd = service.findOne(filter);

                        if (ObjectUtils.isEmpty(dd))
                            service.insert(o, false);
                    } else
                        service.insert(o, false);

                    // 主键关联，返回子表主键; 非主键关联，返回指定的关联字段值
                    Object shipDate = StringUtils.equals("id", m2m.joinColumn()) ? JoinUtil.getId(o) : JoinUtil.getValue(o, m2m.joinColumn());

                    ret.add(shipDate);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void save(Object v) {// 存储中间表
        dto.build(v);
        IbaseMapper baseMapper = (IbaseMapper) this.springContextHolder.getBean("ibaseMapper");
        // 1、首先在中间表中删除
        baseMapper.delM2M(dto);
        // 2、然后插入
        baseMapper.insertM2M(dto);
    }

    public void query() {
    }
}
