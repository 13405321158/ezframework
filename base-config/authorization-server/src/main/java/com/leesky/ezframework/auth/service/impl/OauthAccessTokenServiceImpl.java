/**
 * 
 * @author:weilai
 * @Data:2020-9-189:27:20
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.auth.service.impl;

import com.leesky.ezframework.auth.mapper.IoauthAccessTokenMapper;
import com.leesky.ezframework.auth.model.OauthAccessToken;
import com.leesky.ezframework.auth.service.IoauthAccessTokenService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OauthAccessTokenServiceImpl extends LeeskyServiceImpl<IoauthAccessTokenMapper,OauthAccessToken> implements IoauthAccessTokenService {

}
