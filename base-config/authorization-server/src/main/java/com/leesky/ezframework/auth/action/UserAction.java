/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:58
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.action;

import com.leesky.ezframework.auth.dto.UserBaseDTO;
import com.leesky.ezframework.auth.model.UserBaseModel;
import com.leesky.ezframework.auth.service.IuserBaseService;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * <li>描述:
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class UserAction {

    private TokenEndpoint tokenEndpoint;

    @Autowired
    private IuserBaseService iuserBaseService;

    /**
     * <li>: 新增用户
     *
     * @作者: 魏来
     * @日期: 2021/11/17  上午9:01
     **/
    @GetMapping("/c01")
    public AjaxJson<String> index01(@RequestBody UserBaseDTO dto) {
        AjaxJson<String> json = new AjaxJson();
        try {
            QueryFilter<UserBaseModel> filter = new QueryFilter<>();
            filter.eq("username", dto.getUsername()).select("id");
            UserBaseModel exist = this.iuserBaseService.findOne(filter);
            Assert.isTrue(ObjectUtils.isEmpty(exist), "User name already exists, please re-enter!");

            UserBaseModel user = new UserBaseModel(dto.getUsername(), dto.getPassword());
            this.iuserBaseService.addUser(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }

    /**
     * <li>:
     *
     * @作者: 魏来
     * @日期: 2021/11/17  下午7:11
     **/
    @GetMapping("/token")
    @ApiOperation(value = "说明")
    public AjaxJson postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) {
        AjaxJson json = new AjaxJson();
        try {
            OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
            json.setData(accessToken);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }
}
