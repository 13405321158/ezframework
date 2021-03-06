package com.leesky.ezframework.order.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.order.dto.RetDTO;
import com.leesky.ezframework.order.model.ChildModel;
import com.leesky.ezframework.order.model.CourseModel;
import com.leesky.ezframework.order.model.TeacherModel;
import com.leesky.ezframework.order.service.IChildService;
import com.leesky.ezframework.query.ParamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/child")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChildController {

    @Autowired
    private IChildService childService;

    @ResponseBody
    @RequestMapping(value = "child/{id}")
    public ChildModel get(@PathVariable("id") Long id) {
        ChildModel childModel = childService.findOne(id);
        return childModel;
    }

    @ResponseBody
    @RequestMapping(value = "childs")
    public List<ChildModel> list() {

        List<ChildModel> childModels = childService.findList();

        return childModels;
    }

    @PostMapping("/c01")
    public Result index01() {
        Result json = new Result();
        try {
            ChildModel child = new ChildModel("魏浩然");
            List<CourseModel> c = Lists.newArrayList(new CourseModel("语文"), new CourseModel("数学"), new CourseModel("化学", "00b429e6-3e1e-48e7-a6e4-f2428fa5ae0b"));
            Set<TeacherModel> t = Sets.newHashSet(new TeacherModel("张老师"), new TeacherModel("杨老师"), new TeacherModel("仝老师", "02fe3b27-4a7e-4c53-b984-3d30a561add7"));
            child.setCours(c);
            child.setTeacher(t);
            this.childService.insert(child, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    @PostMapping("/r01")
    public Result index02(@RequestBody ParamModel param) {

        Result json = new Result();
        try {

            QueryFilter<ChildModel> filter = new QueryFilter<>();
            filter.select("cours.name");

            Page<RetDTO> data = this.childService.page(filter, RetDTO.class);

            json.setCount(data.getTotal());
            json.setData(data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
