/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:53
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.service.impl;

import com.leesky.ezframework.auth.mapper.IuserMapper;
import com.leesky.ezframework.auth.model.OauthClientDetailsModel;
import com.leesky.ezframework.auth.model.UserBaseModel;
import com.leesky.ezframework.auth.service.IoauthClientDetailsService;
import com.leesky.ezframework.auth.service.IuserBaseService;
import com.leesky.ezframework.auth.utils.BcryptUtil;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <li>描述:
 */
@Service
public class UserBaseServiceImpl extends LeeskyServiceImpl<IuserMapper, UserBaseModel> implements IuserBaseService {

    @Autowired
    private IoauthClientDetailsService ioauthClientDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("1213");
        return null;
    }

    /**
     * <li>: 增加用户
     *
     * @作者: 魏来
     * @日期: 2021/11/17  上午10:26
     **/
    @Override
    public void addUser(UserBaseModel user) {

        user.setPassword(BcryptUtil.getBcryptEncoder(user.getPassword()));

        this.insert(user, false);

        OauthClientDetailsModel client = new OauthClientDetailsModel(user.getUsername(), user.getPassword());
        this.ioauthClientDetailsService.insert(client,false);
    }
}
