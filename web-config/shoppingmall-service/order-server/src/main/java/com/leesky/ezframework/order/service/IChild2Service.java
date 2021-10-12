package com.leesky.ezframework.order.service;

import com.leesky.ezframework.mybatis.service.IleeskyService;
import com.leesky.ezframework.order.model.Child2;

import java.util.List;


public interface IChild2Service extends IleeskyService<Child2> {
    List<Child2> list2();

    Child2 getById1(Long id);
}