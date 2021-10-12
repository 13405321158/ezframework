package com.leesky.ezframework.order.service.impl;

import org.springframework.stereotype.Service;

import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.order.mapper.IcompanyMapper;
import com.leesky.ezframework.order.model.CompanyModel;
import com.leesky.ezframework.order.service.ICompanyService;



@Service
public class CompanyServiceImpl extends LeeskyServiceImpl<IcompanyMapper, CompanyModel> implements ICompanyService {

}
