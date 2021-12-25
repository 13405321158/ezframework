package com.leesky.ezframework.order.action;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.order.dto.ManDTO;
import com.leesky.ezframework.order.model.*;
import com.leesky.ezframework.order.service.IChildService;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/man")
@SuppressWarnings({"rawtypes"})
public class ManController {

    @Autowired
    private IManService manService;

    @Autowired
    private IChildService childService;

    /*
     * 例1 查询一条记录：主表查询示例，返回值类型是主表
     */
    @RequestMapping(value = "/r01")
    public Result<ManModel> r01(@RequestBody ParamModel param) {
        QueryFilter<ManModel> filter = new QueryFilter<>(param);

        ManModel manModel = manService.findOne(filter);
        return Result.success(manModel);
    }

    /*
     * 例2 查询一条记录：主表查询示例(根据ship内容同时查询子表)，返回值类型是主表
     */
    @RequestMapping(value = "/r02")
    public Result<ManModel> r02(@RequestBody ParamModel param) {

        QueryFilter<ManModel> filter = new QueryFilter<>(param);
        filter.select("id,name,laopoId");//如果不查询laopoId则无法查询laoPo属性，即使下面已经指定了laoPo

        ManModel manModel = manService.findOne(filter, ImmutableMap.of("laoPo", "id,name"));
        return Result.success(manModel);
    }

    /**
     * 例3 查询一条记录：主表和子表 left join 联合查询，返回值类型自定义
     */
    @RequestMapping(value = "/r03")
    public Result<ManDTO> r03(@RequestBody ParamModel param) {
        QueryFilter<ManModel> filter = new QueryFilter<>(param);
        filter.select("id,name,laoPo.name as laopoId");

        ManDTO data = this.manService.findOne(filter, ManDTO.class);
        return Result.success(data);
    }

    /**
     * 例4 查询多条记录：主表
     *
     * @author： 魏来
     * @date: 2021/12/25 下午3:26
     */
    @RequestMapping(value = "/r04")
    public Result<List<ManModel>> r04(@RequestBody ParamModel param) {
        QueryFilter<ManModel> filter = new QueryFilter<>(param);
        filter.select("id");
        List<ManModel> data = this.manService.findList(filter);

        return Result.success(data);
    }

    /**
     * 例5 查询多条记录：主表(根据ship内容同时查询子表)
     */
    @RequestMapping(value = "/r05")
    public Result<List<ManModel>> r05(@RequestBody ParamModel param) {
        QueryFilter<ManModel> filter = new QueryFilter<>(param);
        filter.select("id");
        List<ManModel> data = this.manService.findList(filter, ImmutableMap.of("laoPo", "id,name"));

        return Result.success(data);
    }

    /**
     * 例6 查询多条记录：主表(根据ship内容同时查询子表)
     */
    @RequestMapping(value = "/r06")
    public Result<List<ManDTO>> r06(@RequestBody ParamModel param) {
        QueryFilter<ManModel> filter = new QueryFilter<>(param);
//        filter.select("id");
        List<ManDTO> data = this.manService.findList(filter, ManDTO.class);
        return Result.success(data);
    }

    @GetMapping(value = "mans")
    public List<ManModel> listMans() {
        List<ManModel> list = manService.findList();

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
    public Result index03() {
        List<CourseModel> course = Lists.newArrayList(new CourseModel("语文"), new CourseModel("数学"), new CourseModel("英语"));
        Set<TeacherModel> teacher = Sets.newHashSet(new TeacherModel("张老师"), new TeacherModel("王老师"), new TeacherModel("孙老师"));
        ManModel manModel = new ManModel();
        WomanModel womanModel = new WomanModel();
        ChildModel c = new ChildModel();

        c.setCours(course);
        c.setTeacher(teacher);
        c.setLaoHan(manModel);
        c.setLaoMa(womanModel);

        this.childService.insert(c, true);

        return Result.success();
    }
//
//	@PostMapping("/r01")
//	public Result index02(@RequestBody ParamModel param) {
//
//		param.setSelect("id,name");
//		QueryFilter<ManModel> filter = new QueryFilter<>();
//
//		Page<RetDTO> data = this.manService.page(filter, RetDTO.class);
//
//		return Result.success(data.getRecords(), data.getTotal());
//	}
//
//	@PostMapping("/r02")
//	public Result<?> index04(@RequestBody ParamModel param) {
//
//		QueryFilter<ManModel> filter = new QueryFilter<>();
//		filter.buildQuery(param, ManModel.class);
//
//		Page<RetDTO> data = this.manService.page(filter, RetDTO.class);
//		return Result.success(data.getRecords(), data.getTotal());
////        ManModel s = this.manService.findOne(filter, ImmutableMap.of("laoPo", "id,name", "company", "id,name", "childs", "id,name", "idCard", "id,card_no"));
////
////        RetDTO ret = Po2DtoUtil.convertor(s, RetDTO.class);
////        return Result.success(ret);
//	}
//
//	@PostMapping("/r03")
//	public Result<ChildModel> index05(@RequestBody ParamModel param) {
//
////        param.setSelect("id,name");
//		QueryFilter<ChildModel> filter = new QueryFilter<>();
//		filter.select("id,name");
//
//		ChildModel s = this.childService.findOne(filter, ImmutableMap.of("cours", "id,name", "teacher", "name"));
//
////        RetDTO ret = Po2DtoUtil.convertor(s, RetDTO.class);
//		return Result.success(s);
//	}
}
