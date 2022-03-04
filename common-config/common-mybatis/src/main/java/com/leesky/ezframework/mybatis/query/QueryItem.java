/**
 * 查询子表，并把查询结果赋值给主表entity
 *
 * @author： 魏来
 * @date： 2021/12/16 上午8:29
 */
package com.leesky.ezframework.mybatis.query;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;
import com.leesky.ezframework.mybatis.utils.JoinUtil;
import com.leesky.ezframework.utils.Hump2underline;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;



@Component
@RequiredArgsConstructor
public class QueryItem<T> {

    private final ObjectFactory<SqlSession> factory;

    /**
     * t是主表，ship是需要查询的子表
     *
     * @author： 魏来
     * @date: 2021/12/16 上午8:43
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void query(T entity, ImmutableMap<String, String> ship) {
        // 比如当前查询 媳妇信息，这里entity是上一步查询出来的数据，entity=老公实体类，ship中有个value=wife（wife是entity的属性，其上面标注@One2one注解）
        for (Map.Entry<String, String> entry : ship.entrySet()) {
            String k = entry.getKey();
            String v = StringUtils.isBlank(entry.getValue()) ? "*" : entry.getValue();
            try {
                //1、获取 k对应的字段，即o2o、o2m、m2o、m2m注解所在的字段
                Field f = JoinUtil.getField(entity,k);
                OneToOne o2o = f.getAnnotation(OneToOne.class);
                ManyToOne m2o = f.getAnnotation(ManyToOne.class);
                OneToMany o2m = f.getAnnotation(OneToMany.class);
                ManyToMany m2m = f.getAnnotation(ManyToMany.class);

                FieldCondition<T> fc = new FieldCondition(entity.getClass(), f, false, factory);
                IeeskyMapper objectMapper = (IeeskyMapper) factory.getObject().getMapper(fc.getEntityMapper().targetMapper());

                //2、获取关系注解在对应字段的值，为下一步查询做准备。比如当前查询老公的媳妇，下面4行代码获取媳妇的id值
                String shipId = Hump2underline.lineToHump(f.getAnnotation(JoinColumn.class).name());
                Field shipField = JoinUtil.getField(entity, shipId);
                Objects.requireNonNull(shipField).setAccessible(true);
                Object entityShipValue = shipField.get(entity);


                //3. 一对一、多对一 使用 selectOne方法查询
                if (ObjectUtils.isNotEmpty(o2o) || ObjectUtils.isNotEmpty(m2o)) {

                    //3.1、如果是null值，则不用下面的查询了。比如当前查询老公的媳妇，而在老公数据表中老婆的id=null，那还查个毛
                    if (ObjectUtils.isNotEmpty(entityShipValue)) {
                        QueryFilter filter = common(v, f, entityShipValue);
                        Object obj = objectMapper.selectOne(filter);
                        BeanUtils.setProperty(entity, k, obj);//把查询结果赋值
                    }
                }


                //4. 一对多 使用selectList 方法查询
                if (ObjectUtils.isNotEmpty(o2m)) {
                    QueryFilter filter = common(v, f, entityShipValue);
                    List obj = objectMapper.selectList(filter);
                    BeanUtils.setProperty(entity, k, obj);//把查询结果赋值
                }


                //5. 多对多 需要联合中间表查询
                if (ObjectUtils.isNotEmpty(m2m)) {
                    M2mParam param = getParam(entity, fc, f, v, entityShipValue);
                    List obj = objectMapper.findM2M(param);
                    if (CollectionUtils.isNotEmpty(obj)) {
                        List c = Po2DtoUtil.convertor(obj, fc.getFieldClass());//转换为对象，否则就是map结构

                        if (f.getType().getTypeName().equals("java.util.Set"))
                            BeanUtils.setProperty(entity, k, Sets.newHashSet(c));//把查询结果赋值
                        else
                            BeanUtils.setProperty(entity, k, c);//把查询结果赋值
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        }


    }

    private QueryFilter<?> common(String v, Field f, Object entityShipValue) {
        QueryFilter<?> filter = new QueryFilter<>();
        filter.select(hump2underline(v)).eq(f.getAnnotation(JoinColumn.class).referencedColumnName(), entityShipValue);
        return filter;
    }

    private M2mParam getParam(T entity, FieldCondition<T> fc, Field f, String v, Object shipId) {
        String mainName = JoinUtil.getTableName(entity.getClass());//主表
        String resultName = JoinUtil.getTableName(fc.getFieldClass());//结果表
        String middleName = JoinUtil.getTableName(f.getAnnotation(EntityMapper.class).entityClass());//中间表

        String mainKey = JoinUtil.getTableKeyName(entity.getClass());//主表主键
        String joinColumn = f.getAnnotation(JoinColumn.class).referencedColumnName();//主表 在中间表的名称
        String inverseColumn = f.getAnnotation(InverseJoinColumn.class).referencedColumnName();//结果表在中间表的名称

        String rid = JoinUtil.getTableKeyName(fc.getFieldClass());//结果表主键

        return new M2mParam(hump2underline(v), mainName, middleName, resultName, shipId, mainKey, joinColumn, inverseColumn, rid);
    }

    private String hump2underline(String v) {
        List<String> list = Lists.newArrayList();
        String[] array = StringUtils.split(v, ",");
        Arrays.stream(array).forEach(e -> list.add(Hump2underline.build(e)));
        return StringUtils.join(list, ",");
    }
}
