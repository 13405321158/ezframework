package com.leesky.ezframework.order.action;

import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.order.model.ManModel;
import com.leesky.ezframework.order.model.WomanModel;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/man")
public class ManController {

	@Autowired
	private IManService manService;
//	@Autowired
//	private AutoMapper autoMapper;

	@RequestMapping(value = "man/{id}")
	public ManModel getMan(@PathVariable("id") Long id) {
		ManModel manModel = manService.findOne(id);
		return manModel;
	}

	@GetMapping(value = "mans")
	public List<ManModel> listMans() {
		List<ManModel> list = manService.findAll();

		return list;
	}

	@RequestMapping(value = "mans2")
	public List<ManModel> listMansLazySomeProperty() {
		List<ManModel> list = manService.listMansOneConnectionMoreAutoMapper();

		return list;
	}

	@PostMapping("/c01")
	public AjaxJson index(@RequestBody ParamModel param) {
		AjaxJson json = new AjaxJson();
		try {
			
			WomanModel womanModel = new WomanModel("赵雅芝");
			ManModel manModel = new ManModel(womanModel);
			this.manService.insert(manModel,true);

		} catch (Exception e) {
			json.setSuccess(false, e.getMessage());
		}

		return json;
	}

}
