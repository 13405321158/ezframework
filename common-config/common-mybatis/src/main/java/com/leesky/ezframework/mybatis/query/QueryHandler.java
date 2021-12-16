package com.leesky.ezframework.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/16 上午8:29
 */

@Component
@RequiredArgsConstructor
@SuppressWarnings("ALL")
public class QueryHandler<T> {

    private final ObjectFactory<SqlSession> factory;

    /**
     * 在t中查询 带有ship内容的 field,然后根据 field的注解,查询数据表
     *
     * @author： 魏来
     * @date: 2021/12/16 上午8:43
     */
    public void query(Object retClz, ImmutableMap<String, String> ship) {
        // 比如当前查询 媳妇信息，这里retClz是上一步查询出来的数据，entity=老公实体类，ship中有个value=laopo（laop是entity的属性，其上面标注@One2one注解）
        ship.forEach((k, v) -> {

            try {
                //1、获取 k对应的字段，即o2o、o2m、m2o、m2m注解所在的字段
                Field f = retClz.getClass().getDeclaredField(k);

                //2、获取关系注解在对应字段的值，为下一步查询做准备。比如当前查询老公的媳妇，下面4行代码获取媳妇的id值
                String shipId = Hump2underline.lineToHump(f.getAnnotation(JoinColumn.class).name());
                Field shipField = retClz.getClass().getDeclaredField(shipId);
                shipField.setAccessible(true);
                Object entityShipValue = shipField.get(retClz);

                //2、如果是null值，则不用下面的查询了。比如当前查询老公的媳妇，而在老公数据表中老婆的id=null，那还查个毛
                if (ObjectUtils.isNotEmpty(entityShipValue)) {
                    FieldCondition<T> fc = new FieldCondition(retClz.getClass(), f, false, factory);
                    IeeskyMapper objectMapper = (IeeskyMapper) factory.getObject().getMapper(fc.getEntityMapper().targetMapper());

                    //2.1 一对一、多对一 使用 selectOne方法查询
                    if (ObjectUtils.isNotEmpty(f.getAnnotation(OneToOne.class)) || ObjectUtils.isNotEmpty(f.getAnnotation(ManyToOne.class))) {
                        QueryWrapper<?> filter = new QueryWrapper<>();
                        filter.select(v);//select 内容
                        filter.eq(f.getAnnotation(JoinColumn.class).referencedColumnName(), entityShipValue);
                        Object obj = objectMapper.selectOne(filter);
                        BeanUtils.setProperty(retClz, k, obj);//把查询结果赋值
                    }
                    //2.2 一对多 使用findAll 方法查询
                    if (ObjectUtils.isNotEmpty(f.getAnnotation(OneToMany.class))) {

                    }
                    //2.3 多对多 需要联合中间表查询
                    if (ObjectUtils.isNotEmpty(f.getAnnotation(ManyToMany.class))) {

                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        });


    }
}
