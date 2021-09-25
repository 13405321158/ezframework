package com.leesky.ezframework.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.order.model.Child;
import com.leesky.ezframework.order.service.IChildService;

@RestController
@RequestMapping(value = "/")
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

}
