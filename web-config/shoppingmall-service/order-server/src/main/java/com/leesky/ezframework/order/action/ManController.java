package com.leesky.ezframework.order.action;

import com.leesky.ezframework.mybatis.mapper.AutoMapper;
import com.leesky.ezframework.order.model.Man;
import com.leesky.ezframework.order.service.IManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



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
		Man man = manService.getById(id);
		return man;
	}

	
//	@ResponseBody

	@GetMapping(value = "mans")
	public List<Man> listMans() {
		List<Man> list = manService.list();
		autoMapper.mapperEntityList(list,"company");
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
