package com.leesky.ezframework.order.service;

import java.util.List;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.ChildModel;


public interface IChildService extends IbaseService<ChildModel> {
    List<ChildModel> listByInitialize();

    List<ChildModel> listByInitialize2();

    List<ChildModel> listByInitialize3();
}