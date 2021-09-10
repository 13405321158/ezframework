/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.join.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.leesky.ezframework.join.interfaces.many2many.Many2Many;
import com.leesky.ezframework.join.interfaces.many2many.Many2manyDTO;
import com.leesky.ezframework.join.interfaces.many2many.Many2manyHandler;
import com.leesky.ezframework.join.interfaces.many2one.Many2One;
import com.leesky.ezframework.join.interfaces.many2one.Many2oneHandler;
import com.leesky.ezframework.join.interfaces.one2many.One2Many;
import com.leesky.ezframework.join.interfaces.one2many.One2manyHandler;
import com.leesky.ezframework.join.interfaces.one2one.One2One;
import com.leesky.ezframework.join.interfaces.one2one.One2oneHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaveWithRelation<T> {

    private T entity;

    private BaseMapper<T> baseMapper;

    private final One2oneHandler<T> one2oneHandler;
    private final One2manyHandler one2manyHandler;
    private final Many2manyHandler many2manyHandler;
    private final Many2oneHandler many2oneHandler;

    /**
     * @author: weilai
     * @Data: 2020-8-1910:44:25
     * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
     * @Desc: 关联关系 分为主表和从表，含义参见各自类说明
     * @Desc: 先存储从表后存储主表，因为存储完毕后实体类才有主键，才能把得到到主键 赋值给 主表中对应字段（非主键关联谁先谁后存储，无所谓）
     */
    public void relationship(T entity, BaseMapper<T> baseMapper) {
        this.entity = entity;
        this.baseMapper = baseMapper;

        List<One2oneHandler<T>> o2oList = Lists.newArrayList();
        List<Many2manyHandler> m2mList = Lists.newArrayList();
        List<One2manyHandler> o2mList = Lists.newArrayList();

        // 1、查找出当前实体entity中的所有字段
        List<Field> fields = JoinUtil.getAllField(entity);

        // 2、遍历字段，找出：one2one、many2many、many2one、one2many 关系
        for (Field f : fields) {

            // 2.1 one2one关系
            One2One o2o = f.getAnnotation(One2One.class);
            if (ObjectUtils.isNotEmpty(o2o)) {
                String rf = o2o.joinField();
                if (StringUtils.isNotBlank(rf))
                    JoinUtil.setValue(entity, rf, getRelation(f, entity, o2o));// f.get(entity)是从表，立刻存储，获取关联关系到主键，并赋值给主表
                else
                    o2oList.add(this.one2oneHandler.build(f, entity, o2o.otherOneTableColumn()));// f.get(entity)是主表，先保存起来，遍历完毕再存储
            }
            // 2.2 many2many关系
            Many2Many m2m = f.getAnnotation(Many2Many.class);
            if (ObjectUtils.isNotEmpty(m2m)) {
                Multimap multimap = many2manyHandler.build(f, entity, m2m).save();// 存储另一个many方
                if (CollectionUtils.isNotEmpty(multimap.get("ids")))
                    m2mList.add(this.many2manyHandler.build(new Many2manyDTO(m2m, multimap)));// 保存待存储中间表
            }
            // 2.3 one2many关系
            One2Many one2many = f.getAnnotation(One2Many.class);
            if (ObjectUtils.isNotEmpty(one2many))
                o2mList.add(this.one2manyHandler.build(f, entity, one2many.joinField()));

            // 2.4 many2one关系
            Many2One many2one = f.getAnnotation(Many2One.class);
            if (ObjectUtils.isNotEmpty(many2one))
                this.many2oneHandler.build(f, entity, many2one.joinField()).save();

        }
        this.baseMapper.insert(entity);

        Object key = JoinUtil.getId(entity);
        o2oList.forEach(e -> e.save(key));// 处理one2one
        m2mList.forEach(e -> e.save(key));// 存储many2many的中间表
        o2mList.forEach(e -> e.save(key));// 存储one2many中的 many方

    }

    /**
     * @作者: 魏来
     * @日期: 2021年8月23日 下午5:29:48
     * @描述: 存储实体中的关联数据，适用于：one2one
     */
    private Object getRelation(Field f, Object entity, One2One one2one) {

        Object obj = one2oneHandler.build(f, entity, one2one.joinField()).save();

        // 主键关联，返回子表主键; 非主键关联，返回指定的关联字段值
        return StringUtils.equals("id", one2one.otherOneTableColumn()) ? JoinUtil.getId(obj) : JoinUtil.getValue(obj, one2one.otherOneTableColumn());

    }

}
