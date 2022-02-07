/*
 * @作者: 魏来
 * @日期: 2021-08-21 15:48:39
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.backend.service;

import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.model.UserBaseModel;
import com.leesky.ezframework.mybatis.service.IeeskyService;

import java.util.List;

public interface IuserBaseService extends IeeskyService<UserBaseModel> {

    /**
     * <li>新增账户，同时新增对应client</li>
     *
     * @作者: 魏来
     * @日期: 2021年12月3日 上午9:32:05
     */
    void addUser(UserBaseDTO dto) throws Exception;

    /**
     * 编辑用户
     *
     * @author： 魏来
     * @date: 2022/1/29  16:17
     */
    void editUser(UserBaseModel dto) throws Exception;

    /**
     * 删除用户：cbm_mag_user、cbm_mag_user_ext01、cbm_mag_user_ext02、oauth_client_details
     *
     * @author： 魏来
     * @date: 2022/1/26  13:06
     */
    void delUser(List<UserBaseDTO> list);
}
