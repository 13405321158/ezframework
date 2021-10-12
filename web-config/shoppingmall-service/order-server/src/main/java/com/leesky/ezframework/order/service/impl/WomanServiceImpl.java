package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.IwomanMapper;
import com.leesky.ezframework.order.model.WomanModel;
import com.leesky.ezframework.order.service.IWomanService;
import org.springframework.stereotype.Service;


@Service
public class WomanServiceImpl extends LeeskyServiceImpl<IwomanMapper, WomanModel> implements IWomanService {

}
