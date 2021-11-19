package com.leesky.ezframework.auth.service.impl;

import com.leesky.ezframework.auth.mapper.IoauthClientDetailsMapper;
import com.leesky.ezframework.auth.model.OauthClientDetailsModel;
import com.leesky.ezframework.auth.service.IoauthClientDetailsService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: weilai
 * @Data:下午1:56:33,2020年1月3日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>描述此类的作用
 */
@Service
public class OauthClientDetailsServiceImpl extends LeeskyServiceImpl<IoauthClientDetailsMapper, OauthClientDetailsModel> implements IoauthClientDetailsService {

	@Autowired
	private IoauthClientDetailsMapper repo;

	/**
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:下午3:49:04,2020年1月3日
	 * @desc:
	 *        <li>增加客户端
	 *
	 */
	@Override
	@Transactional
	public void addClient(OauthClientDetailsModel entity) {
		this.repo.insert(entity);
	}

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
	@Override
	public OauthClientDetailsModel findByclientId(String clientId) {

		return this.repo.findByclientId(clientId);
	}

	/**
	 * @ver: 1.0.0
	 * @author: weilai
	 * @data:上午10:34:36,2020年1月13日
	 * @desc:
	 *        <li>
	 *
	 */
	@Override
	public void editClitetPwd(OauthClientDetailsModel entity) {
		this.repo.insert(entity);

	}

}
