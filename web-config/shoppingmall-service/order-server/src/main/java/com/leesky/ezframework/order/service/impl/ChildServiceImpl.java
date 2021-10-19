package com.leesky.ezframework.order.service.impl;

import org.springframework.stereotype.Service;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.model.ChildModel;
import com.leesky.ezframework.order.service.IChildService;



@Service
public class ChildServiceImpl extends LeeskyServiceImpl<IchildMapper, ChildModel> implements IChildService {

}
