/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:37
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * <li>描述: token 存储方式： 数据表 还是redis
 */
public class JdbcTokenStores extends JdbcTokenStore {

    public JdbcTokenStores(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = null;

        try {
            accessToken = new DefaultOAuth2AccessToken(tokenValue);
        } catch (EmptyResultDataAccessException | IllegalArgumentException e) {
            removeAccessToken(tokenValue);
        }

        return accessToken;
    }
}
