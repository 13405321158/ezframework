package com.leesky.ezframework.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.order.model.Man;
import com.leesky.ezframework.order.service.IManService;



@RestController
@RequestMapping(value = "/")
public class ManController {

	@Autowired
	private IManService manService;
	@Autowired
	private AutoMapper autoMapper;

	@ResponseBody
	@RequestMapping(value = "man/{id}")
	public Man getMan(@PathVariable("id") Long id) {
		Man man = manService.findOne(id);
		return man;
	}

	
//	@ResponseBody

	@GetMapping(value = "mans")
	public List<Man> listMans() {
		List<Man> list = manService.findAll();
//		autoMapper.mapperEntityList(list,"laoPo");
//		autoMapper.mapperEntityList(list,"tels");
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "mans2")
	public List<Man> listMansLazySomeProperty() {
		List<Man> list = manService.listMansOneConnectionMoreAutoMapper();

		return list;
	}
	

}
