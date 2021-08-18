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

	List<E> list();

	List<E> list(ParamModel model);

	E findById(ObjectId pk);

	List<E> findById(List<ObjectId> pk);

	Page<E> page(ParamModel model);



    E save(E entity);

    void saveBatch(List<E> list);

    void update(ObjectId id, E t);

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



    void deleteById(ObjectId pk);

}
