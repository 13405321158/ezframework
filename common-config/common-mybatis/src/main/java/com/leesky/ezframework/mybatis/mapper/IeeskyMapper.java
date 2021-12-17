/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午1:08:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 扩展方法
 */
package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.mybatis.query.M2mParam;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.mybatis.save.Many2manyDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IeeskyMapper<T> extends BaseMapper<T> {

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；clz=返回值类型
     * <li>1、构造filter时带有xxxModel.class 参数，xxxModel中含有o2o,o2m,m2o,m2m注解
     * <li>2、如果filter 的select，或者 where 条件中含有"." 则需采用left join查询(此时 ship不起作用)</li>
     * <li>3、依据ship内容做子查询，并把结果赋值给查询结果（如果xxxModel中带有多个o2o,o2m,m2o,m2m的属性值；
     * <li>比如你仅需要查询所有o2o(o2o有多个,你可能需要其中一个)，o2m,m2o,m2m关系不需要查询，则ship中只包含o2o对应的属性值即可
     *
     * @author： 魏来
     * @date: 2021/12/15 下午4:13
     */
    Map<String, Object> findOne(@Param("filter") QueryFilter<T> filter);

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    List<Map<String, Object>> findList(QueryFilter<T> filter);

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；多对多查询，联合中间表
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:15:49
     */
    List<Map<String, Object>> findM2M(M2mParam param);

    /**
     * <li>个性化扩展，最终实现于leeskyMapper.xml,支持多表联合查询；
     *
     * @作者: 魏来
     * @日期: 2021年9月25日 上午8:20:12
     */
    List<Map<String, Object>> page(@Param("filter") QueryFilter<T> filter);

    /**
     * <li>:获取记录总数
     *
     * @作者: 魏来
     * @日期: 2021/10/26  下午1:20
     **/
    Long getTotal(QueryFilter<T> filter);

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
