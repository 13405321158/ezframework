package com.leesky.ezframework.order.service.impl;


import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.Ichild2Mapper;
import com.leesky.ezframework.order.model.Child2;
import com.leesky.ezframework.order.service.IChild2Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class Child2ServiceImpl extends LeeskyServiceImpl<Ichild2Mapper, Child2> implements IChild2Service {

	@Transactional
	public List<Child2> list2() {
		List<Child2> list = this.list();
		this.autoMapper.initialize(list, "courses");
		return list;
	}

	@Transactional
	@Override
	public Child2 getById1(Long id) {
		Child2 child2 = ((Ichild2Mapper)this.getBaseMapper()).selectById1(id);
		this.autoMapper.initialize(child2, "courses");
		return child2;
	}

}
