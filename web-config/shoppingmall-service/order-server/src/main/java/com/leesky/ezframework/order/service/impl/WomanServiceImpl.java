package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.BaseServiceImpl;
import com.leesky.ezframework.order.mapper.WomanMapper;
import com.leesky.ezframework.order.model.Woman;
import com.leesky.ezframework.order.service.IWomanService;
import org.springframework.stereotype.Service;


@Service
public class WomanServiceImpl extends BaseServiceImpl<WomanMapper, Woman> implements IWomanService {

}
