/**
 * 
 * @author:weilai
 * @Data:2020-8-1819:12:10
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.join.interfaces.many2may.ManyToManyDto;
import com.leesky.ezframework.join.interfaces.many2one.ManyToOne;
import com.leesky.ezframework.join.interfaces.one2many.OneToMany;
import com.leesky.ezframework.join.interfaces.one2one.OneToOne;

import java.util.HashMap;
import java.util.List;

public interface LeeskyMapper<T> extends BaseMapper<T> {

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2011:25:59
	 * @Desc:
	 *        <li>ManyToMany 注解查询
	 */
	List<HashMap<String, Object>> many2manyQuery(ManyToManyDto param, String value);

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2011:27:28
	 * @Desc:
	 *        <li>OneToOne 注解查询
	 */
	HashMap<String, Object> one2oneQuery(OneToOne param, String value);

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:44:41
	 * @Desc:
	 *        <li>ManyToOne 注解查询
	 */
	HashMap<String, Object> many2oneQuery(ManyToOne param, String value);

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2016:29:53
	 * @Desc:
	 *        <li>OneToMany 注解查询
	 */
	List<HashMap<String, Object>> one2manyQuery(OneToMany param, String value);

	/**
	 * 
	 * @author: weilai
	 * @Data:2021年1月8日上午9:00:10
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
