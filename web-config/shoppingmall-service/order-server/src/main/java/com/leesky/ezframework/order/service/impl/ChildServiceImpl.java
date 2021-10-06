package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.BaseServiceImpl;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.model.Child;
import com.leesky.ezframework.order.service.IChildService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class ChildServiceImpl extends BaseServiceImpl<IchildMapper, Child> implements IChildService {
	@Transactional
	public List<Child> listByInitialize() {
			List<Child> list = this.list();
			this.autoMapper.initialize(list, "laoHan","laoMa","courses","teachers");
			return list;
	}
	
	@Transactional
	public List<Child> listByInitialize2() {
			List<Child> list = this.list();
			this.initialize(list, "laoHan","laoMa","courses","teachers");
			return list;
	}
	

	@Transactional
	public List<Child> listByInitialize3() {
			List<Child> list = this.list();
			this.initialize(list, "laoHan","laoMa","courses");
			return list;
	}
}
