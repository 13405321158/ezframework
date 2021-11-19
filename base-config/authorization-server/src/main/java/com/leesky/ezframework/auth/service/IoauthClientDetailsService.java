package com.leesky.ezframework.auth.service;

import com.leesky.ezframework.auth.model.OauthClientDetailsModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;

/**
 * @author: weilai
 * @Data:下午1:55:51,2020年1月3日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:<li>描述此类的作用
 */
public interface IoauthClientDetailsService extends IeeskyService<OauthClientDetailsModel> {

	
	
	/**
	 * 
	 * @Author: weilai
	 * @Data:Nov 9, 2019 4:25:48 PM
	 * @Desc:
	 *        <li>新增客户端
	 *
	 */
	void addClient(OauthClientDetailsModel entity);
	

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
	
	
	/**
	 * 
	 * @author weilai
	 * @desc 修改客户端密码，只允许修改密码
	 * @date 2018年10月30日上午11:31:59
	 * @param entity
	 */
	void editClitetPwd(OauthClientDetailsModel entity);
}
