/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午4:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.action;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.leesky.ezframework.backend.model.*;
import com.leesky.ezframework.backend.service.IdealerOrderItemService;
import com.leesky.ezframework.backend.service.IgroupService;
import com.leesky.ezframework.backend.service.IuserBaseService;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.query.ParamModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestAction {

    private final IgroupService igroupService;

    private final IuserBaseService iuserBaseService;
//    private final IdealerOrderService idealerOrderService;
    private final IdealerOrderItemService idealerOrderItemService;

    @Transactional
    @RequestMapping("/c01")
    public AjaxJson index01(@RequestBody ParamModel model) {

        AjaxJson json = new AjaxJson();

        try {
            DealerOrderModel order = new DealerOrderModel();

            List<DealerOrderItemModel> list = Lists.newArrayList(new DealerOrderItemModel(order), new DealerOrderItemModel(order), new DealerOrderItemModel(order));

            order.setItems(list);

//			this.idealerOrderService.insert(order, true);

            this.idealerOrderItemService.insert(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getLocalizedMessage());
        }
        return json;
    }


    @PostMapping("/c02")
    public AjaxJson index02() {
        AjaxJson json = new AjaxJson();
        try {
            UserBaseExt01Model e1 = new UserBaseExt01Model();
            UserBaseExt02Model e2 = new UserBaseExt02Model();

            UserBaseModel user = new UserBaseModel(e1, e2);

            this.iuserBaseService.insert(user, true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }
    @Transactional
    @PostMapping("/c03")
    public AjaxJson index03() {
        AjaxJson json = new AjaxJson();
        try {
            UserBaseModel user = new UserBaseModel();
            Set<GroupModel> set = Sets.newHashSet(new GroupModel(),new GroupModel());
            user.setGroupSet(set);

            this.iuserBaseService.insert(user, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }
    @Transactional
    @PostMapping("/c04")
    public AjaxJson index04() {
        AjaxJson json = new AjaxJson();
        try {
            GroupModel group = new GroupModel();

            UserBaseModel a = new UserBaseModel();
            a.setId("5e92a7699a1585774ab11ff961650d73");
            UserBaseModel b = new UserBaseModel();
//            b.setId("0de6e45d35f01f80f03968e3eb589d60");

            Set<UserBaseModel> set = Sets.newHashSet(a,b);
            group.setUserSet(set);

            this.igroupService.insert(group, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }
    @GetMapping("/r01")
    public AjaxJson index05() {
        AjaxJson json = new AjaxJson();
        try {
            ImmutableMap<String, String> param = ImmutableMap.of("ext01_select", "*");
            List<UserBaseModel> data = this.iuserBaseService.findAll(param);
            json.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }


    @GetMapping("/r02")
    public AjaxJson index06() {
        AjaxJson json = new AjaxJson();
        try {
            ImmutableMap<String, String> param = ImmutableMap.of("ext01_select", "*");
            GroupModel data = this.igroupService.leek("weilai");
            json.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }
}
