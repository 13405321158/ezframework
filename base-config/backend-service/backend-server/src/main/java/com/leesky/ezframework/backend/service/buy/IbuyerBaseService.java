package com.leesky.ezframework.backend.service.buy;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.buyer.BuyerBaseModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午2:34
 */
public interface IbuyerBaseService extends IeeskyService<BuyerBaseModel> {

    /**
     * <li>新增账户，同时新增对应client</li>
     *
     * @author: 魏来
     * @date: 2022/2/26 上午10:14
     */
    void addUser(UserBaseDTO dto) throws Exception;
}
