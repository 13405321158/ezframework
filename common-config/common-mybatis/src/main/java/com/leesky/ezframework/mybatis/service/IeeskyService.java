package com.leesky.ezframework.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.mybatis.query.QueryFilter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IeeskyService<T> {

    /**
     * <li>根据记录主键查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    T findOne(Serializable id);

    /**
     * <li>自定义查询条件，返回一条记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    T findOne(QueryFilter<T> filter);

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
    T findOne(QueryFilter<T> filter, ImmutableMap<String, String> ship);

    /**
     * <li>查询全部
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    List<T> findList();

    /**
     * <li>根据主键集合查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    List<T> findList(Collection<? extends Serializable> idList);

    /**
     * <li>根据wrapper过滤器 查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    List<T> findList(QueryFilter<T> filter);

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；retClz=返回值类型
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    <E> List<E> findList(QueryFilter<T> filter, Class<E> retClz);

    /**
     * <li>根据wrapper过滤器 分页查询
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    Page<T> page(QueryFilter<T> filter);

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；retClz=返回值类型
     * <li> retClz: 返回类型
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    <E> Page<E> page(QueryFilter<T> filter, Class<E> retClz);


    /**
     * <li>relation=false 不处理聚合关系</li>
     * <li>relation=true 则同时存储one2one、many2many，one2Many，many2one 关系</li>
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午4:48
     **/
    void insert(T entity, Boolean withRelation);

    /**
     * 描述: 批量插入数据
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:51
     **/
    void insert(List<T> entityList, Boolean withRelation);

    /**
     * <li>:根据Id更新
     *
     * @作者: 魏来
     * @日期: 2021/10/13  下午2:34
     **/
    void update(T entity);

    /**
     * <li>: 自定义条件更新
     *
     * @作者: 魏来
     * @日期: 2021/10/13  下午2:35
     **/
    void update(UpdateWrapper<T> filter);

    /**
     * <li>: 批量更新
     *
     * @作者: 魏来
     * @日期: 2021/10/13  下午2:38
     **/
    void update(Collection<T> entityList);

    /**
     * <li>: 根据id删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    void delete(Serializable id);

    /**
     * <li>:根据条件删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    void delete(QueryFilter<T> filter);

    /**
     * <li>: 批量删除
     *
     * @作者: 魏来
     * @日期: 2021/10/15  下午1:54
     **/
    void delete(Collection<? extends Serializable> idList);
}