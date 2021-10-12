package com.leesky.ezframework.order.action;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.order.model.ChildModel;
import com.leesky.ezframework.order.model.CourseModel;
import com.leesky.ezframework.order.model.TeacherModel;
import com.leesky.ezframework.order.service.IChildService;

@RestController
@RequestMapping(value = "/child")
public class ChildController {
//    @Autowired
//    private AutoMapper autoMapper;

    @Autowired
    private IChildService childService;

    @ResponseBody
    @RequestMapping(value = "child/{id}")
    public ChildModel get(@PathVariable("id") Long id) {
        ChildModel childModel = childService.findOne(id,false);
        return childModel;
    }

    @ResponseBody
    @RequestMapping(value = "childs")
    public List<ChildModel> list() {

        List<ChildModel> childModels = childService.findAll(false);

        return childModels;
    }

    @GetMapping("/c01")
    public AjaxJson index01() {
        AjaxJson json = new AjaxJson();
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
}
