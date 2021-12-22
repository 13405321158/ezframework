package com.leesky.ezframework.mybatis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;
import com.leesky.ezframework.mybatis.query.Common;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.mybatis.query.QueryHandler;
import com.leesky.ezframework.mybatis.save.SaveHandler;
import com.leesky.ezframework.mybatis.service.IeeskyService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuppressWarnings("unchecked")
public class LeeskyServiceImpl<M extends IeeskyMapper<T>, T> implements IeeskyService<T> {

    @Autowired
    private M baseMapper;
    @Autowired
    private AutoMapper autoMapper;
    @Autowired
    private SaveHandler<T> saveHandler;
    @Autowired
    private QueryHandler<T> queryHandler;


    private Class<T> entityClass = currentModelClass();
    private Class<M> mapperClass = currentMapperClass();

    private final String msg = "QueryFilter的tableName参数=null,请执行：filter.buildQuery(参数01,xxxModel.class)";

    /**
     * 描述: 根据记录主键查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    @Transactional(readOnly = true)
    public T findOne(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    /**
     * 描述: 自定义查询条件，返回一条记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    @Transactional(readOnly = true)
    public T findOne(QueryFilter<T> filter) {
        return this.baseMapper.selectOne(filter);
    }

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；clz=返回值类型
     * <li>1、构造filter时带有xxxModel.class 参数，xxxModel中含有o2o,o2m,m2o,m2m注解
     * <li>2、如果filter 的select，或者 where 条件中含有"." 则需采用left join查询(此时 ship不起作用)</li>
     * <li>3、依据ship内容做子查询，并把结果赋值给查询结果（如果xxxModel中带有多个o2o,o2m,m2o,m2m的属性值；
     * <li>比如你仅需要查询所有o2o(o2o有多个,你可能需要其中一个)，o2m,m2o,m2m关系不需要查询，则ship中只包含o2o对应的属性值即可
     *
     * @author： 魏来
     * @date: 2021/12/15 下午3:22
     */
    @Override
    @Transactional(readOnly = true)
    public T findOne(QueryFilter<T> filter, ImmutableMap<String, String> ship) {
        Assert.isTrue(StringUtils.isNotBlank(filter.getTableName()), this.msg);//检查构造filter时带有xxxModel.class参数了吗，
        Boolean isJoin = isJoinQuery(filter, ship);
        Map<String, Object> data = this.baseMapper.findOne(filter);
        T result = JSON.parseObject(JSONObject.toJSONString(data), this.entityClass);

        if (isJoin)//查询 ship中的数据，并赋值给result
            this.queryHandler.query(result, ship);

        return result;
    }

    /**
     * 描述: 查询全部
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    public List<T> findList() {
        return this.baseMapper.selectList(Wrappers.emptyWrapper());
    }

    /**
     * 描述:根据wrapper过滤器 查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    @Override
    public List<T> findList(QueryFilter<T> filter) {
        return this.baseMapper.selectList(filter);
    }

    /**
     * 描述: 根据主键集合查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    @Override
    public List<T> findList(Collection<? extends Serializable> keys) {
        return this.baseMapper.selectBatchIds(keys);
    }


    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；clz=返回值类型
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    @Override
    public <E> List<E> findList(QueryFilter<T> filter, Class<E> clz) {
        List<Map<String, Object>> data = this.baseMapper.findList(filter);
        return JSON.parseArray(JSONObject.toJSONString(data), clz);
    }

    /**
     * 描述:根据wrapper过滤器 分页查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    @Override
    public Page<T> page(QueryFilter<T> filter) {
        Integer size = filter.getParam().getLimit();
        Integer offset = filter.getParam().getPage();
        Page<T> page = new Page<>(offset, size);

        return this.baseMapper.selectPage(page, filter);
    }

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；retClz=返回值类型
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    @Override
    public <E> Page<E> page(QueryFilter<T> filter, Class<E> retClz) {
        Assert.isTrue(StringUtils.isNotBlank(filter.getTableName()), this.msg);
        filter.select(Common.buildSelect(filter.getSqlSelect()));//防止select 内容是通过 filter.select 设置，而不是通过param.select 设置

        Page<E> page = new Page<>();
        Long total = this.baseMapper.getTotal(filter);
        List<Map<String, Object>> data = this.baseMapper.page(filter);
        List<E> bean = JSON.parseArray(JSONObject.toJSONString(data), retClz);

        page.setTotal(total);
        page.setRecords(bean);
        return page;
    }

    /**
     * <li>relation=false 不处理聚合关系</li>
     * <li>relation=true 则同时存储one2one、many2many，one2Many，many2one 关系</li>
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午4:48
     **/
    @Override
    public void insert(T entity, Boolean withRelation) {

        if (withRelation)
            this.autoMapper.insert(entity, this.baseMapper, saveHandler);
        else
            this.baseMapper.insert(entity);
    }

    /**
     * 描述: 批量插入数据
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:51
     **/
    @Override
    public void insert(List<T> entityList, Boolean withRelation) {
        if (withRelation)
            this.autoMapper.insert(entityList, this.baseMapper, saveHandler);
        else
            this.baseMapper.insertBatch(entityList);
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(T entity) {
        this.baseMapper.updateById(entity);
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(UpdateWrapper<T> filter) {
        this.baseMapper.update(null, filter);
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/10/13 下午2:41
     **/
    @Override
    public void update(Collection<T> entityList) {
        this.baseMapper.updateBatch(entityList);
    }

    /**
     * <li>: 根据id删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15 下午1:54
     **/
    @Override
    public void delete(Serializable id) {
        this.baseMapper.deleteById(id);
    }

    /**
     * <li>:根据条件删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15 下午1:54
     **/
    @Override
    public void delete(QueryFilter<T> filter) {
        this.baseMapper.delete(filter);
    }

    /**
     * <li>: 批量删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15 下午1:54
     **/
    @Override
    public void delete(Collection<? extends Serializable> idList) {
        this.baseMapper.deleteBatchIds(idList);
    }

    private Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), LeeskyServiceImpl.class, 0);
    }

    private Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), LeeskyServiceImpl.class, 1);
    }

    /**
     * 判断 s1,或者s2的keySet 中是否还有"."。 含有. 则说明需要left join 查询
     *
     * @author： 魏来
     * @date: 2021/12/15 下午5:15
     */
    private Boolean isJoinQuery(QueryFilter<T> filter, ImmutableMap<String, String> ship) {
        String s1 = filter.getSqlSelect();//select 内容
        Map<String, Object> s2 = filter.getP1();// where 内容

        s1 = s1.replace("a.", "");
        if (s1.contains("."))
            return false;
        for (String str : s2.keySet())
            if (str.contains("."))
                return false;

        return !MapUtils.isEmpty(ship);
    }
}