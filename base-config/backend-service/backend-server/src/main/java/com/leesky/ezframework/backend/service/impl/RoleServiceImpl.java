package com.leesky.ezframework.backend.service.impl;

import com.leesky.ezframework.backend.mapper.IroleMapper;
import com.leesky.ezframework.backend.model.RoleModel;
import com.leesky.ezframework.backend.service.IroleService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import org.springframework.stereotype.Service;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/10 下午1:39
 */
@Service
public class RoleServiceImpl extends LeeskyServiceImpl<IroleMapper, RoleModel> implements IroleService {
}
