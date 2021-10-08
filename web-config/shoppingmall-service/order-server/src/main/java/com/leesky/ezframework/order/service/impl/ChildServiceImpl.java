package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.BaseServiceImpl;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.model.ChildModel;
import com.leesky.ezframework.order.service.IChildService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class ChildServiceImpl extends BaseServiceImpl<IchildMapper, ChildModel> implements IChildService {
	@Transactional
	public List<ChildModel> listByInitialize() {
			List<ChildModel> list = this.list();
			this.autoMapper.initialize(list, "laoHan","laoMa","courses","teachers");
			return list;
	}
	
	@Transactional
	public List<ChildModel> listByInitialize2() {
			List<ChildModel> list = this.list();
			this.initialize(list, "laoHan","laoMa","courses","teachers");
			return list;
	}
	

	@Transactional
	public List<ChildModel> listByInitialize3() {
			List<ChildModel> list = this.list();
			this.initialize(list, "laoHan","laoMa","courses");
			return list;
	}
}
