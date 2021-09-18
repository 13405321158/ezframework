package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.Child;

import java.util.List;


public interface IChildService extends IbaseService<Child> {
    List<Child> listByInitialize();

    List<Child> listByInitialize2();

    List<Child> listByInitialize3();
}