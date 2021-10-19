package com.leesky.ezframework.order.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.order.model.ChildModel;
import com.leesky.ezframework.order.model.CompanyModel;
import com.leesky.ezframework.order.model.IdCardModel;
import com.leesky.ezframework.order.model.ManModel;
import com.leesky.ezframework.order.model.TelModel;
import com.leesky.ezframework.order.model.WomanModel;
import com.leesky.ezframework.order.service.IManService;
import com.leesky.ezframework.query.ParamModel;

@RestController
@RequestMapping(value = "/man")
public class ManController {

	@Autowired
	private IManService manService;


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

	@PostMapping("/c01")
	public AjaxJson index(@RequestBody ParamModel param) {
		AjaxJson json = new AjaxJson();
		try {

			HashSet<TelModel> tels = Sets.newHashSet(new TelModel(), new TelModel());
			List<ChildModel> c = Lists.newArrayList(new ChildModel(), new ChildModel());

			CompanyModel company = new CompanyModel("神化集团");
			IdCardModel c1 = new IdCardModel(RandomStringUtils.randomNumeric(20), "北京市海淀区王庄路1号");
			IdCardModel c2 = new IdCardModel(RandomStringUtils.randomNumeric(20), "青岛市市北区湖光山色小区");
//
			WomanModel womanModel = new WomanModel();
			ManModel manModel = new ManModel(womanModel);
//			manModel.setCompanyModel(company);
//			manModel.setIdCard(c2);
//			manModel.setTels(tels);
//			manModel.setChilds(c);
//
//			womanModel.setIdCard(c1);
//			womanModel.setLaoGong(manModel);
//
//
//            this.idCardService.insert(Lists.newArrayList(c1, c2));
			this.manService.insert(Lists.newArrayList(manModel), true);
//            this.iwomanService.insert(womanModel,true);
//
//            UpdateWrapper<ManModel> filter = new UpdateWrapper<>();
//            filter.eq("id", manModel.getId());
//            filter.set("laoPo_id", womanModel.getId());
//            this.manService.update(filter);

		} catch (Exception e) {
			json.setSuccess(false, e.getMessage());
		}

		return json;
	}

	@PostMapping("/c02")
	public AjaxJson index03(@RequestBody ParamModel param) {
		AjaxJson json = new AjaxJson();
		try {
			ManModel manModel = new ManModel();
//            this.manService.insert(manModel);

			List<ChildModel> c = Lists.newArrayList(new ChildModel(manModel), new ChildModel(manModel));
			manModel.setChilds(c);
//            this.childService.insert(c);

			Set<TelModel> s = Sets.newHashSet(new TelModel(manModel), new TelModel(manModel), new TelModel(manModel));
			manModel.setTels(s);
//            this.telService.insert(Lists.newArrayList(s));

		} catch (Exception e) {
			json.setSuccess(false, e.getMessage());
		}
		return json;
	}

	@PostMapping("/r01")
	public AjaxJson index02(@RequestBody ParamModel param) {
		AjaxJson json = new AjaxJson();
		try {
			QueryFilter<ManModel> filter = new QueryFilter<>(param);

			Page<ManModel> result = this.manService.page(filter);

			json.setCount(result.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false, e.getMessage());
		}

		return json;
	}
}
