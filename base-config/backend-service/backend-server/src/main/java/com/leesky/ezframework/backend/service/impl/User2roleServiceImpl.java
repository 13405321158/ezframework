package com.leesky.ezframework.backend.service.impl;

import com.google.common.collect.Lists;
import com.leesky.ezframework.backend.mapper.Iuser2roleMapper;
import com.leesky.ezframework.backend.model.User2RoleModel;
import com.leesky.ezframework.backend.service.Iuser2roleService;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.query.CommonDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/10 下午3:04
 */
@Service
public class User2roleServiceImpl extends LeeskyServiceImpl<Iuser2roleMapper, User2RoleModel> implements Iuser2roleService {


    /**
     * 增加 角色_用户 对应关系
     *
     * @author： 魏来
     * @date: 2021/12/10 下午3:34
     */
    @Override
    @Transactional
    public void addUser2role(CommonDTO dto) {

        //1、先删除当前角色 已有的用户关系
        QueryFilter<User2RoleModel> filter = new QueryFilter<>();
        filter.eq("role_id", dto.getPid());
        this.delete(filter);

        List<User2RoleModel> list = Lists.newArrayList();
        for (String uid : dto.getCid())
            list.add(new User2RoleModel());

        //2、再插入新数据
        this.insert(list, false);
    }
}
