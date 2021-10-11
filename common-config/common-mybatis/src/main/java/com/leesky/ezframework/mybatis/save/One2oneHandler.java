/*
 * @作者: 魏来
 * @日期: 2021年9月27日  下午2:51:29
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.condition.TableIdCondition;
import com.leesky.ezframework.mybatis.mapper.IbaseMapper;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * 类功能说明：
 * <li>处理实体entity中的one2one关系</li>
 * <li>fields是entity中带有one2one注解的属性集合
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class One2oneHandler<T> {

    private final ObjectFactory<SqlSession> factory;

    /**
     * <li>:fields 是 entity中的 one2one关系方</li>
     * <li>设计思路：</li>
     * <li>a、循环判断fields中的实体是否可以存储</li>
     * <li>b、可以存储条件是：实体中id=null，或者经过查询后数据表中=null</li>
     * <li>c、不可以存储条件是：实体中id！=null，且经过查询后数据表中存在记录</li>
     * <li>d、如果可以存储，则判断双方是否有引用，如果有引用则把对方id记录下来</li>
     *
     * @作者: 魏来
     * @日期: 2021/9/29 上午9:30
     **/
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void handler(String[] fields, T entity, IbaseMapper ibaseMapper) throws Exception {

        String entityKey = UUID.randomUUID().toString();
        TableIdCondition tc = new TableIdCondition(entity.getClass());
        BeanUtils.setProperty(entity, tc.getFieldOfTableId().getName(), entityKey);

        for (String f : fields) {

            boolean insert = true;

            Field field = entity.getClass().getDeclaredField(f);
            field.setAccessible(true);
            Object o2o = field.get(entity);// obj 就是要存储的实体（entity中 的 one2one关系实体）

            if (ObjectUtils.isNotEmpty(o2o)) {

                FieldCondition<T> fc = new FieldCondition<>(entity, field, false, factory);

                // 1、在数据库查询o2o是否存在
                String v = BeanUtils.getProperty(o2o, fc.getFieldOfTableId().getName());// o2o对象的主键值
                Class<?> mapperClass = fc.getEntityMapper().targetMapper();
                String dbColumn = fc.getTableId().value();
                IbaseMapper o2oMapper = (IbaseMapper) factory.getObject().getMapper(mapperClass);

                if (StringUtils.isNotBlank(v)) {

                    Long otherOne = o2oMapper.selectCount(new QueryFilter<>().select(dbColumn).eq(dbColumn, v));// 去数据表中查询
                    if (otherOne > 0)
                        insert = false;

                }

                // 2、如果数据表中不存在 o2o 则插入之；同时设置 v=主键值，为下一步向entity中关联字段赋值做准备
                if (insert) {
                    o2oMapper.insert(o2o);
                    v = BeanUtils.getProperty(o2o, fc.getFieldOfTableId().getName());
                }

                // 3、判断entity中是否有 o2o的引用，如果有，则把o2o的主键 赋值给entity中对应属性
                String n = fc.getJoinColumn().name();
                if (!StringUtils.equals(n, "id")) // 不等于 id则认为 o2o 在entity对应的数据表中有 关联字段
                    BeanUtils.setProperty(entity, Hump2underline.lineToHump(n), v);

                // 4、判断o2o对象中是否有 对entity实体对引用，如果有：则更新一下(在2中做了保存动作，所以这里更新)
                Field[] fs = o2o.getClass().getDeclaredFields();
                String n1 = field.getDeclaringClass().getName();
                for (Field o : fs) {
                    if (StringUtils.equals(o.getGenericType().getTypeName(), n1)) {
                        String j = o.getAnnotation(JoinColumn.class).name();
                        UpdateWrapper uw = new UpdateWrapper();
                        uw.eq(dbColumn, v);
                        uw.set(j, entityKey);
                        o2oMapper.update(null, uw);
                        break;
                    }
                }
            }

        }
        // 5、存储 entity
        ibaseMapper.insert(entity);
    }
}
