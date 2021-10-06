package com.leesky.ezframework.order.service;

import java.util.List;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.Child;


public interface IChildService extends IbaseService<Child> {
    List<Child> listByInitialize();

    List<Child> listByInitialize2();

    List<Child> listByInitialize3();
}