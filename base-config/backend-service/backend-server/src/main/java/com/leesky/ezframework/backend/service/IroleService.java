package com.leesky.ezframework.backend.service;

import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;

import java.util.List;

public interface IroleService extends IeeskyService<RoleModel> {

    /**
     * 根据用户id查询对应角色编码
     *
     * @author： 魏来
     * @date: 2021/12/11 下午4:38
     */
    List<RoleModel> getRoleCodes(String userId);


}
