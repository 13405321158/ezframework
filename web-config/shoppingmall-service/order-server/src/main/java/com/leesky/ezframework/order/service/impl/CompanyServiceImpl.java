package com.leesky.ezframework.order.service.impl;

import org.springframework.stereotype.Service;

import com.leesky.ezframework.mybatis.service.impl.BaseServiceImpl;
import com.leesky.ezframework.order.mapper.IcompanyMapper;
import com.leesky.ezframework.order.model.Company;
import com.leesky.ezframework.order.service.ICompanyService;



@Service
public class CompanyServiceImpl extends BaseServiceImpl<IcompanyMapper, Company> implements ICompanyService {

}
