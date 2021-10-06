package com.leesky.ezframework.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.order.model.Man;
import com.leesky.ezframework.order.model.Woman;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;

@RestController
@RequestMapping(value = "/man")
public class ManController {

	@Autowired
	private IManService manService;
//	@Autowired
//	private AutoMapper autoMapper;

	@RequestMapping(value = "man/{id}")
	public Man getMan(@PathVariable("id") Long id) {
		Man man = manService.findOne(id);
		return man;
	}

	@GetMapping(value = "mans")
	public List<Man> listMans() {
		List<Man> list = manService.findAll();

		return list;
	}

	@RequestMapping(value = "mans2")
	public List<Man> listMansLazySomeProperty() {
		List<Man> list = manService.listMansOneConnectionMoreAutoMapper();

		return list;
	}

	@PostMapping("/c01")
	public AjaxJson index(@RequestBody ParamModel param) {
		AjaxJson json = new AjaxJson();
		try {
			
			Woman woman = new Woman("赵雅芝");
			Man man = new Man(woman);
			this.manService.insert(man,true);

		} catch (Exception e) {
			json.setSuccess(false, e.getMessage());
		}

		return json;
	}

}
