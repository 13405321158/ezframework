/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午1:08:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.mapper;

import java.util.Collection;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.mybatis.save.Many2manyDTO;

public interface IbaseMapper<T> extends BaseMapper<T> {

    /**
     * <li>插入数据到： many2many 到中间表</li>
     *
     * @作者: 魏来
     * @日期: 2021年8月25日 下午4:46:01
     */
    void insertM2M(Many2manyDTO model);

    /**
     * <li>中级表数据插入前先删除
     *
     * @作者: 魏来
     * @日期: 2021年8月27日 上午9:11:38
     */
    void delM2M(Many2manyDTO model);

    /**
     * <li>批量插入</li>
     *
     * @作者: 魏来
     * @日期: 2021年9月1日 下午3:51:35
     */
    int insertBatch(Collection<T> list);

    /**
     * <li>批量更新</li>
     *
     * @作者: 魏来
     * @日期: 2021年9月1日 下午3:51:35
     */
    int updateBatch(Collection<T> list);

//T leek(String id);
//
//    /**
//     * <li>OneToOne 注解查询</li>
//     *
//     * @author:weilai
//     * @Data: 2020-8-2011:27:28
//     */
//    HashMap<String, Object> one2oneQuery(One2One o2o, Object value, String selectColumn);
//
//    /**
//     * <li>ManyToMany 注解查询
//     *
//     * @author:weilai
//     * @Data: 2020-8-2011:25:59
//     */
//    List<HashMap<String, Object>> many2manyQuery(Many2Many m2m, Object value,String selectColumn);
}
