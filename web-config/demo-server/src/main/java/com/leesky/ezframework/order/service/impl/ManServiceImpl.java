package com.leesky.ezframework.order.service.impl;

import org.springframework.stereotype.Service;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.ImanMapper;
import com.leesky.ezframework.order.model.ManModel;
import com.leesky.ezframework.order.service.IManService;


@Service
public class ManServiceImpl extends LeeskyServiceImpl<ImanMapper, ManModel> implements IManService {

}
