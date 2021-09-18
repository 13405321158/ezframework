package com.leesky.ezframework.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leesky.ezframework.order.model.Child2;


public interface Child2Mapper extends BaseMapper<Child2> {
	public Child2 selectById1(Long id);
}
