package com.leesky.ezframework.auth.mapper;

import com.leesky.ezframework.auth.model.OauthClientDetailsModel;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;

/**
 * @author weilai
 * @data 2018年10月30日 上午10:48:27
 *
 * @desc 类描述
 *       <li>
 */

public interface IoauthClientDetailsMapper extends IeeskyMapper<OauthClientDetailsModel> {

	/**
	 * 
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:下午6:21:52,2020年1月9日
	 * @desc:
	 *        <li>编辑用户信息时使用
	 *        <li>参数clientId = EncrypMD5Util.eccrypt(username)
	 *
	 */
	OauthClientDetailsModel findByclientId(String clientId);
	
	

}
