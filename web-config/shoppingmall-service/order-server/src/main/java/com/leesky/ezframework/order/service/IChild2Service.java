package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IbaseService;
import com.leesky.ezframework.order.model.Child2;

import java.util.List;


public interface IChild2Service extends IbaseService<Child2> {
    List<Child2> list2();

    Child2 getById1(Long id);
}