package com.leesky.ezframework.order.service.impl;

import com.leesky.ezframework.mybatis.service.impl.BaseServiceImpl;
import com.leesky.ezframework.order.mapper.ImanMapper;
import com.leesky.ezframework.order.model.ManModel;
import com.leesky.ezframework.order.service.IManService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ManServiceImpl extends BaseServiceImpl<ImanMapper, ManModel> implements IManService {
    @Transactional
    public List<ManModel> listMansOneConnectionMoreAutoMapper() {
        return this.list();

    }
}
