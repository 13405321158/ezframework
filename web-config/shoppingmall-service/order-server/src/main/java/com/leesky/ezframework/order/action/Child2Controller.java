package com.leesky.ezframework.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.order.model.Child2;
import com.leesky.ezframework.order.service.IChild2Service;

@RestController
@RequestMapping(value = "/")
public class Child2Controller {
	@Autowired
	private IChild2Service child2Service;

	@ResponseBody
	@RequestMapping(value = "child2/{id}")
	public Child2 get(@PathVariable("id") Long id) {
		Child2 child = child2Service.findOne(id);
		return child;
	}

	@ResponseBody
	@RequestMapping(value = "child2list")
	public List<Child2> list() {
		List<Child2> childs = child2Service.findAll();

		return childs;
	}

}
