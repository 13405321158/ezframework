package com.leesky.ezframework.backend.action;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.backend.dto.UserAuthDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.utils.Po2DtoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/1 下午6:39
 */

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserAction {

    private final IuserBaseService service;

    @GetMapping("/{username}")
    public AjaxJson<UserAuthDTO> getUserByUsername(@PathVariable String username) {
        AjaxJson<UserAuthDTO> json = new AjaxJson<>();
        try {

            QueryFilter<UserBaseModel> filter = new QueryFilter<>();
            filter.select("id,username,status,password");
            filter.eq("username", username);
            UserBaseModel user = this.service.findOne(filter);

            UserAuthDTO dto = Po2DtoUtil.convertor(user, UserAuthDTO.class);
            json.setData(dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }

        return json;
    }
}
