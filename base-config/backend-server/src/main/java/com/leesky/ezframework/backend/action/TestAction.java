/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午4:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.action;

import com.leesky.ezframework.backend.model.UserBaseExt01Model;
import com.leesky.ezframework.backend.model.UserBaseExt02Model;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.query.ParamModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestAction {

    private final IuserBaseService iuserBaseService;


    @RequestMapping("/r01")
    public AjaxJson index01(@RequestBody ParamModel model) {

        AjaxJson json = new AjaxJson();

        try {
            UserBaseExt01Model ext01 = new UserBaseExt01Model();
            UserBaseExt02Model ext02 = new UserBaseExt02Model();
            UserBaseModel user = new UserBaseModel(ext01, ext02);

            this.iuserBaseService.insert(user,true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }

}
