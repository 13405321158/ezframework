/*
 * @作者: 魏来
 * @日期: 2022/2/24 上午7:45
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.details.userdetails.saler;

import com.leesky.ezframework.backend.api.IbackendServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/24 上午7:45
 */
@Service
@RequiredArgsConstructor
public class SalerDetailsService implements UserDetailsService {

    private final IbackendServerClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        this.client.loadSalerUserByUsername(username);
        return null;
    }
}
