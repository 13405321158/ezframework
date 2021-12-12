package com.leesky.ezframework.backend.mapper;

import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.backend.model.User2RoleModel;
import com.leesky.ezframework.mybatis.mapper.IeeskyMapper;

import java.util.List;

public interface Iuser2roleMapper extends IeeskyMapper<User2RoleModel> {

    /**
     * 根据用户id查询对应角色编码
     *
     * @author： 魏来
     * @date: 2021/12/11 下午4:38
     */
    List<RoleModel> getRoleCodes(String userId);
}
