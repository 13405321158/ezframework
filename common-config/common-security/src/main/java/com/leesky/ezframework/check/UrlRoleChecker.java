package com.leesky.ezframework.check;

import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.global.Redis;
import com.leesky.ezframework.redis.service.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <li>基于角色的权限控制器</li>
 *
 * @author: 魏来
 * @date: 2022/2/28 下午7:10
 */
@Component("roleChecker")
public class UrlRoleChecker implements RoleChecker {

    @Autowired
    private RedisService redisService;

    @SuppressWarnings("unchecked")
    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
        // 1、预检请求放行
        if (request.getMethod().toUpperCase().equals(HttpMethod.OPTIONS.name()))
            return true;

        PathMatcher pathMatcher = new AntPathMatcher();

        //2、获取当前URL
        String path = request.getRequestURI();

        //3、URL在白名单内则直接放行
        for (String w : Common.WHITE_LIST) {
            if (pathMatcher.match(path, w))
                return true;
        }

        //4、获取当前登录用户具有的角色集合
        Map<String, Object> claims = ((Jwt) authentication.getPrincipal()).getClaims();
        List<String> userRoles = (List<String>) claims.get(Common.ROLE_LIST);
        Assert.isTrue(CollectionUtils.isNotEmpty(userRoles), claims.get("user_name") + "无权访问" + path);

        //5、鉴权开始: 缓存取 [URL权限-角色集合] 规则数据
        List<String> urlRoles = (List<String>) this.redisService.getHash(Redis.URL_ROLES_KEY, path);
        Assert.isTrue(CollectionUtils.isNotEmpty(urlRoles), "路径[" + path + "]未分配到任何角色");

        List<String> same = userRoles.stream().filter(urlRoles::contains).collect(Collectors.toList());
        return !CollectionUtils.isEmpty(same);
    }


}
