/*
 * @作者: 魏来
 * @日期: 2021年11月20日  上午10:56:55
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.ext.refres;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 类功能说明：
 * <li>刷新token重新授权
 */
public class RefresTokenGranter<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {

	@Override
	public void afterPropertiesSet()  {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserDetails loadUserDetails(T token) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
