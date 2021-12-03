/*
 * @作者: 魏来
 * @日期: 2021-08-21 15:48:39
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.backend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.mapper.IuserBaseMapper;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IoauthCientService;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.utils.Po2DtoUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBaseServiceImpl extends LeeskyServiceImpl<IuserBaseMapper, UserBaseModel> implements IuserBaseService {

	@Value("${access.token.validity:360}") // 默认值过期时间60*60s 一小时
	private int accessTokenValiditySeconds;

	@Value("${access.refresh.validity:360}")
	private int refreshTokenValiditySeconds;

	private final PasswordEncoder passwordEncoder;

	private final IoauthCientService clientService;

	/**
	 * <li>新增账户，同时新增对应client</li>
	 * <li>控制器中已经验证了 数据表中无重重复账户信息
	 * 
	 * @作者: 魏来
	 * @日期: 2021年12月3日 上午9:32:05
	 */
	@Override
	@Transactional
	public void addUser(UserBaseDTO dto) {
		String pwd = passwordEncoder.encode(dto.getPassword());

		UserBaseModel model = Po2DtoUtil.convertor(dto, UserBaseModel.class);
		model.setPassword(pwd);
		this.insert(model, false);

		OauthClientDetailsModel client = new OauthClientDetailsModel(dto.getUsername(), pwd, accessTokenValiditySeconds, refreshTokenValiditySeconds);
		this.clientService.insert(client, false);

	}

}
