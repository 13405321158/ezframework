/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午1:08:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.join.mapper;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.join.interfaces.many2many.Many2manyDTO;
import com.leesky.ezframework.join.interfaces.many2many.Many2Many;
import com.leesky.ezframework.join.interfaces.many2one.Many2One;
import com.leesky.ezframework.join.interfaces.one2many.One2Many;
import com.leesky.ezframework.join.interfaces.one2one.One2One;

public interface IbaseMapper<T> extends BaseMapper<T> {

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月27日 上午9:11:38
	 * @描述: 中级表数据变换前先删除
	 */
	void delM2M(Many2manyDTO model);

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月25日 下午4:46:01
	 * @描述: 插入数据到： many2many 到中间表
	 */
	void insertM2M(Many2manyDTO model);

	/**
	 * @author:weilai
	 * @Data: 2020-8-2011:25:59
	 * @Desc:
	 *        <li>ManyToMany 注解查询
	 */
	List<HashMap<String, Object>> many2manyQuery(Many2Many param, String value);

	/**
	 * @author:weilai
	 * @Data: 2020-8-2011:27:28
	 * @Desc:
	 *        <li>OneToOne 注解查询
	 */
	HashMap<String, Object> one2oneQuery(One2One param, String value);

	/**
	 * @author:weilai
	 * @Data: 2020-8-2014:44:41
	 * @Desc:
	 *        <li>ManyToOne 注解查询
	 */
	HashMap<String, Object> many2oneQuery(Many2One param, String value);

	/**
	 * @author:weilai
	 * @Data: 2020-8-2016:29:53
	 * @Desc:
	 *        <li>OneToMany 注解查询
	 */
	List<HashMap<String, Object>> one2manyQuery(One2Many param, String value);

	/**
	 * @author: weilai
	 * @Data: 2021年1月8日上午9:00:10
	 * @Desc:
	 *        <li>
	 */
	List<T> findAll(String param);

	/**
	 * @作者: 魏来
	 * @日期: 2021年8月16日 上午10:53:42
	 * @描述: 返回记录总数
	 */
	Long count(String param);

}
