package com.leesky.ezframework.order.mapper;

import com.leesky.ezframework.mybatis.mapper.IleeskyMapper;
import com.leesky.ezframework.order.model.Child2;


public interface Ichild2Mapper extends IleeskyMapper<Child2> {
	public Child2 selectById1(Long id);
}
