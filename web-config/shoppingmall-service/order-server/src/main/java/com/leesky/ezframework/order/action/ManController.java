package com.leesky.ezframework.order.action;

import com.google.common.collect.Sets;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.order.model.CompanyModel;
import com.leesky.ezframework.order.model.ManModel;
import com.leesky.ezframework.order.model.TelModel;
import com.leesky.ezframework.order.model.WomanModel;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/man")
public class ManController {

    @Autowired
    private IManService manService;


    @RequestMapping(value = "man/{id}")
    public ManModel getMan(@PathVariable("id") Long id) {
        ManModel manModel = manService.findOne(id);
        return manModel;
    }

    @GetMapping(value = "mans")
    public List<ManModel> listMans() {
        List<ManModel> list = manService.findAll();

        return list;
    }

    @RequestMapping(value = "mans2")
    public List<ManModel> listMansLazySomeProperty() {
        List<ManModel> list = manService.listMansOneConnectionMoreAutoMapper();

        return list;
    }

    @PostMapping("/c01")
    public AjaxJson index(@RequestBody ParamModel param) {
        AjaxJson json = new AjaxJson();
        try {

            WomanModel womanModel = new WomanModel("赵雅芝");
            Set<TelModel> list = Sets.newHashSet(new TelModel("13405321158", "8e8c007e3712339d425330ac173d9c21"), new TelModel("15954209390"), new TelModel("15954807365"));
            ManModel manModel = new ManModel(womanModel);
            manModel.setTelModels(list);

            this.manService.insert(manModel, true);

        } catch (Exception e) {
            json.setSuccess(false, e.getMessage());
        }

        return json;
    }

    @PostMapping("/c02")
    public AjaxJson index02(@RequestBody ParamModel param) {
        AjaxJson json = new AjaxJson();
        try {
            CompanyModel company = new CompanyModel("清华同方软件股份","6543b5698d2ddfb515c751c17e0da65c");
            ManModel manModel = new ManModel(company,"魏浩然");
            this.manService.insert(manModel, true);
        } catch (Exception e) {
            json.setSuccess(false, e.getMessage());
        }

        return json;
    }
}
