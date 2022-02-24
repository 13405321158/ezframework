/**
 * @author:weilai
 * @Data:2020年11月12日上午10:14:34
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>
 */
package com.leesky.ezframework.tools.utils;

import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.redis.service.RedisService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@AllArgsConstructor
public class UserContext {


    private final RedisService cache;

    /**
     * 解析JWT获取用户登录名称
     */
    public String getUserName() {

        String token_ = tokenPrefix();
        Object obj = this.cache.get(Common.USER_NAME + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }

    /**
     * 解析JWT获取用户ID
     */
    public String getUserId() {

        String token_ = tokenPrefix();
        Object obj = this.cache.get(Common.USER_ID + "_" + token_);
        return ObjectUtils.isNotEmpty(obj) ? (String) obj : "";
    }


    private String tokenPrefix() {
        String token = getToken();
        return StringUtils.split(token, ".")[0];//token的第一部分值
    }

    private String getToken() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Assert.isTrue(ObjectUtils.isNotEmpty(attr), attr.getRequest().getRequestURL() + ": 请求缺少访问令牌");
        return attr.getRequest().getHeader(Common.URL_HEADER_PARAM).replace(Common.TOKEN_TYPE, "");
    }
}
