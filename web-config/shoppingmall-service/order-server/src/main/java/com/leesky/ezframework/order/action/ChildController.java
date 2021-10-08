package com.leesky.ezframework.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.order.model.Child;
import com.leesky.ezframework.order.model.Course;
import com.leesky.ezframework.order.service.IChildService;

@RestController
@RequestMapping(value = "/child")
public class ChildController {
    @Autowired
    private AutoMapper autoMapper;

    @Autowired
    private IChildService childService;

    @ResponseBody
    @RequestMapping(value = "child/{id}")
    public Child get(@PathVariable("id") Long id) {
        Child child = childService.findOne(id);
        return child;
    }

    @ResponseBody
    @RequestMapping(value = "childs")
    public List<Child> list() {

        List<Child> childs = childService.findAll();

        return childs;
    }

    @GetMapping("/c01")
    public AjaxJson index01() {
        AjaxJson json = new AjaxJson();
        try {
            Child child = new Child("魏浩然");
            List<Course> course = Lists.newArrayList(new Course("语文"), new Course("数学","04b9be9a-451b-4f59-86e3-6f9759c6ad4e"), new Course("化学", "00b429e6-3e1e-48e7-a6e4-f2428fa5ae0b"));
//            Set<Teacher> teacher = Sets.newHashSet(new Teacher("张老师"), new Teacher("杨老师"), new Teacher("仝老师", "02fe3b27-4a7e-4c53-b984-3d30a561add7"));
            child.setCourses(course);
//            child.setTeachers(teacher);
            this.childService.insert(child, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
