package com.leesky.ezframework.backend.service;

import com.leesky.ezframework.backend.model.User2RoleModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;
import com.leesky.ezframework.query.CommonDTO;

public interface Iuser2roleService extends IeeskyService<User2RoleModel> {
    /**
     * 增加 角色_用户 对应关系
     *
     * @author： 魏来
     * @date: 2021/12/10 下午3:34
     */
    void addUser2role(CommonDTO dto);
}
