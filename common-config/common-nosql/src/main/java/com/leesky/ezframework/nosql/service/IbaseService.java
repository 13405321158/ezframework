/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.nosql.service;

import com.leesky.ezframework.query.ParamModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface IbaseService<E, ObjectId> {
    /**
     * 查询全部数据
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:32
     */
    List<E> list();

    /**
     * 根据Id查询数据
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:34
     */
    E findById(ObjectId pk);

    /**
     * 根据queryStr 查询数据
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:33
     */
    List<E> list(ParamModel model);

    /**
     * 根据Id批量查询
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:35
     */
    List<E> findById(List<ObjectId> pk);

    /**
     * 分页查询
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:35
     */
    Page<E> page(ParamModel model);

    /**
     * 存储数据
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:36
     */
    E save(E entity);

    /**
     * 批量存储
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:36
     */
    void saveBatch(List<E> list);

    /**
     * 根据调解更新
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:37
     */
    void update(ObjectId id, E t) throws IllegalAccessException;

    /**
     * 根据id修改
     *
     * @param id             更新主键
     * @param updateFieldMap key:需要更新的属性  value:对应的属性值
     */
    void update(ObjectId id, Map<String, Object> updateFieldMap);

    /**
     * 根据传入值修改
     *
     * @param queryFieldMap  key:查询条件的属性  value:对应的属性值
     * @param updateFieldMap key:需要更新的属性  value:对应的属性值
     */
    void update(Map<String, Object> queryFieldMap, Map<String, Object> updateFieldMap);

    /**
     * 根据主键 删除数据
     *
     * @author: 魏来
     * @date: 2022/2/12 下午3:37
     */
    void deleteById(ObjectId pk);

}
