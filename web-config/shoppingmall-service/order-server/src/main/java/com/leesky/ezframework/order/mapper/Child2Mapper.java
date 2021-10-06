package com.leesky.ezframework.order.mapper;

import com.leesky.ezframework.mybatis.mapper.IbaseMapper;
import com.leesky.ezframework.order.model.Child2;


public interface Child2Mapper extends IbaseMapper<Child2> {
	public Child2 selectById1(Long id);
}
