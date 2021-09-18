package com.leesky.ezframework.order.action;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.order.model.Child2;
import com.leesky.ezframework.order.service.IChild2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/")
public class Child2Controller {
	@Autowired
	private AutoMapper autoMapper;

	@Autowired
	private IChild2Service child2Service;


	@ResponseBody
	@RequestMapping(value = "child2/{id}")
	public Child2 get(@PathVariable("id") Long id) {
		Child2 child = child2Service.getById(id);
		return child;
	}

	@ResponseBody
	@RequestMapping(value = "child2list")
	public List<Child2> list() {
		List<Child2> childs = child2Service.list();

		return childs;
	}
	

	@ResponseBody
	@RequestMapping(value = "child2list2")
	public List<Child2> list2() {
		List<Child2> childs = child2Service.list(new QueryWrapper<Child2>().gt("child_id", 1));

		return childs;
	}
	
	@ResponseBody
	@RequestMapping(value = "test2")
	public String test() {
		List<Child2> childs = child2Service.list();
		System.out.println(childs);
		return "abc";
	}

}
