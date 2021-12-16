package com.leesky.ezframework.order.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.order.dto.RetDTO;
import com.leesky.ezframework.order.model.*;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.Po2DtoUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/man")
@SuppressWarnings({"rawtypes", "unchecked"})
public class ManController {

    @Autowired
    private IManService manService;

    @RequestMapping(value = "man/{id}")
    public ManModel getMan(@PathVariable("id") String id) {
        ManModel manModel = manService.findOne(id);
        return manModel;
    }

    @GetMapping(value = "mans")
    public List<ManModel> listMans() {
        List<ManModel> list = manService.findAll();

        return list;
    }

    @PostMapping("/c01")
    public Result index(@RequestBody ParamModel param) {


        HashSet<TelModel> tels = Sets.newHashSet(new TelModel(), new TelModel());
        List<ChildModel> c = Lists.newArrayList(new ChildModel(), new ChildModel());

        CompanyModel company = new CompanyModel("神化集团");
//            IdCardModel c1 = new IdCardModel(RandomStringUtils.randomNumeric(20), "北京市海淀区王庄路1号");
        IdCardModel c2 = new IdCardModel(RandomStringUtils.randomNumeric(20), "青岛市市北区湖光山色小区");
//
        WomanModel womanModel = new WomanModel();
        ManModel manModel = new ManModel(womanModel);
        manModel.setCompany(company);
        manModel.setIdCard(c2);
        manModel.setTels(tels);
        manModel.setChilds(c);

//			womanModel.setIdCard(c1);
//			womanModel.setLaoGong(manModel);
//
//
//            this.idCardService.insert(Lists.newArrayList(c1, c2));
        this.manService.insert(Lists.newArrayList(manModel), true);
//            this.iwomanService.insert(womanModel,true);
//
//            UpdateWrapper<ManModel> filter = new UpdateWrapper<>();
//            filter.eq("id", manModel.getId());
//            filter.set("laoPo_id", womanModel.getId());
//            this.manService.update(filter);


        return Result.success();
    }

    @PostMapping("/c02")
    public Result index03(@RequestBody ParamModel param) {

        ManModel manModel = new ManModel();
//            this.manService.insert(manModel);

        List<ChildModel> c = Lists.newArrayList(new ChildModel(manModel), new ChildModel(manModel));
        manModel.setChilds(c);
//            this.childService.insert(c);

        Set<TelModel> s = Sets.newHashSet(new TelModel(manModel), new TelModel(manModel), new TelModel(manModel));
        manModel.setTels(s);
//            this.telService.insert(Lists.newArrayList(s));


        return Result.success();
    }

    @PostMapping("/r01")
    public Result index02(@RequestBody ParamModel param) {

        param.setSelect("id,name");
        QueryFilter<ManModel> filter = new QueryFilter<>(param, ManModel.class);

        Page<RetDTO> data = this.manService.page(filter, RetDTO.class);


        return Result.success(data.getRecords(), data.getTotal());
    }


    @PostMapping("/r02")
    public Result index04(@RequestBody ParamModel param) {

        param.setSelect("id,name,laopoId,companyId");
        QueryFilter<ManModel> filter = new QueryFilter<>(param, ManModel.class);

//        Page<RetDTO> data = this.manService.page(filter, RetDTO.class);
//        return Result.success(data.getRecords(), data.getTotal());
        ManModel s = this.manService.findOne(filter, ImmutableMap.of("laoPo", "id,name", "company", "id,name"));

        RetDTO ret = Po2DtoUtil.convertor(s, RetDTO.class);
        return Result.success(ret);
    }
}
