package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IleeskyService;
import com.leesky.ezframework.order.model.ManModel;

import java.util.List;



public interface IManService extends IleeskyService<ManModel> {
	 List<ManModel> listMansOneConnectionMoreAutoMapper();

}