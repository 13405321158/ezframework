package com.leesky.ezframework.backend.service.saler;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.saler.SalerBaseModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午2:33
 */
public interface IsalerBaseService extends IeeskyService<SalerBaseModel> {

    /**
     * <li>新增账户，同时新增对应client</li>
     *
     * @author: 魏来
     * @date: 2022/2/26 上午10:14
     */
    void addUser(UserBaseDTO dto) throws Exception;
}
