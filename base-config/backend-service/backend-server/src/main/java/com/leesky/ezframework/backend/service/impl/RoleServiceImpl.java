package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IroleMapper;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.backend.service.IroleService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/10 下午1:39
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends LeeskyServiceImpl<IroleMapper, RoleModel> implements IroleService {
    private final Iuser2roleMapper user2roleMapper;
    /**
     * 根据用户id查询对应角色编码
     *
     * @author： 魏来
     * @date: 2021/12/11 下午4:38
     */
    @Override
    public List<RoleModel> getRoleCodes(String userId) {
        return this.user2roleMapper.getRoleCodes(userId);
    }
}
