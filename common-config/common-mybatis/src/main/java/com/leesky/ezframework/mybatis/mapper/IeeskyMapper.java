/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午1:08:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.mybatis.save.Many2manyDTO;

import java.util.Collection;

public interface IeeskyMapper<T> extends BaseMapper<T> {


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
    /**
     * <li>插入数据到： many2many 到中间表</li>
     *
     * @作者: 魏来
     * @日期: 2021年8月25日 下午4:46:01
     */
    void insertM2M(Many2manyDTO model);
}
