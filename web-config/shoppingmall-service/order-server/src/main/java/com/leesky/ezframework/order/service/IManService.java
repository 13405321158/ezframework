package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.Man;

import java.util.List;



public interface IManService extends IbaseService<Man> {
	 List<Man> listMansOneConnectionMoreAutoMapper();

}