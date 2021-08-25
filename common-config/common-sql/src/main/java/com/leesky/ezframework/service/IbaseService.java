/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leesky.ezframework.query.QueryFilter;


public interface IbaseService<T> extends IService<T> {

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:39
     * @描述: 根据记录主键查询
     **/
    T getOne(String id);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:39
     * @描述: 自定义查询条件，返回一条记录
     **/
    T getOne(QueryFilter<T> filter);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:40
     * @描述: 自定义查询条件，返回多个记录
     **/
    List<T> list(QueryFilter<T> filter);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:41
     * @描述: 自定义查询条件，分页返回记录
     **/
    Page<T> page(QueryFilter<T> filter);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:48
     * @描述: 插入单个数据
     **/
    void insert(T entity);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午4:48
     * @描述: 存储one2one、many2many，one2Many，many2one 关系
     **/
    void insert(T entity, Boolean relation);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:51
     * @描述: 披露插入数据
     **/
    void insert(Collection<T> entityList);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:52
     * @描述: 根据主键删除一条记录
     **/
    void del(Serializable id);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:55
     * @描述: 自定义删除条件
     **/
    void del(QueryFilter<T> filter);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:56
     * @描述: 根据主键 披露删除一批
     **/
    void del(Collection<? extends Serializable> ids);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午12:57
     * @描述: 更新一条记录
     **/
    void edit(T entity);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午1:14
     * @描述: 批量更新记录
     **/
    void edit(Collection<T> entityList);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午1:14
     * @描述: 自定义更新条件
     **/
    void edit(T entity, Wrapper<T> updateWrapper);

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午1:16
     * @描述: 查询全部数据个数
     **/
    @Override
	int count();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  下午1:16
     * @描述: 根据条件查询记录个数
     **/
    int count(QueryWrapper<T> filter);

}
