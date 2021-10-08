package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.ManModel;

import java.util.List;



public interface IManService extends IbaseService<ManModel> {
	 List<ManModel> listMansOneConnectionMoreAutoMapper();

}