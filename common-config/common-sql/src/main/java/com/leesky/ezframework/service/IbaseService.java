/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leesky.ezframework.query.QueryFilter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IbaseService<T> {

    /**
     * 描述: 根据记录主键查询
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    T findOne(String id);

    /**
     * 描述: 自定义查询条件，返回一条记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:39
     **/
    T findOne(QueryFilter<T> filter);

    /**
     * 描述: 查询全部
     *
     * @作者: 魏来
     * @日期: 2021/9/2  上午10:31
     **/
    List<T> findAll();

    /**
     * 描述: 带o2o, m2m,m2o,o2m映射关系查询
     *
     * @作者: 魏来
     * @日期: 2021/9/2  上午10:31
     **/
    List<T> findAll(Map<String, String> param);

    /**
     * 描述: 自定义查询条件，返回多个记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:40
     **/
    List<T> findAll(QueryFilter<T> filter);

    /**
     * 描述: 自定义查询条件，分页返回记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:41
     **/
    Page<T> findByPage(QueryFilter<T> filter);

    /**
     * 描述: relation=false 不处理聚合关系
     * 描述: relation=true 则同时存储one2one、many2many，one2Many，many2one 关系
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
    void insert(Collection<T> entityList);

    /**
     * 描述: 根据主键删除一条记录
     *
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:52
     **/
    void del(Serializable id);

    /**
     * 描述: 自定义删除条件
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:55
     **/
    void del(QueryFilter<T> filter);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:56
     * @描述: 根据主键 披露删除一批
     **/
    void del(Collection<? extends Serializable> ids);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午12:57
     * @描述: 更新一条记录
     **/
    void edit(T entity);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午1:14
     * @描述: 批量更新记录
     **/
    void edit(Collection<T> entityList);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午1:14
     * @描述: 自定义更新条件
     **/
    void edit(T entity, Wrapper<T> updateWrapper);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午1:16
     * @描述: 查询全部数据个数
     **/
    long count();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21 下午1:16
     * @描述: 根据条件查询记录个数
     **/
    long count(QueryWrapper<T> filter);


    T leek(String id);
}
