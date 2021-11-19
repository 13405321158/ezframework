package com.leesky.ezframework.auth.service;

import com.leesky.ezframework.auth.model.UserBaseModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IuserBaseService extends IeeskyService<UserBaseModel>, UserDetailsService {
    /**
     * <li>: 增加用户
     *
     * @作者: 魏来
     * @日期: 2021/11/17  上午10:26
     **/
    void addUser(UserBaseModel user);
}
