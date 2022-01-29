/*
 * @作者: 魏来
 * @日期: 2021年12月3日  上午9:16:48
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:新增、编辑、查找账户时使用
 */
package com.leesky.ezframework.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBaseDTO {

    private String id;
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    private String nickName;//昵称
    private String mobile;//手机号
    private String status;//账号状态
    private Date byTime; //账户有效期至
    private Date editPwdDate;//修改密码时间
    private String ext01Id;//扩展表01主键
    private String ext02Id;//扩展表02主键
    private String remake;//备注
    private String auditStep;//审核

    private Set<RoleDTO> roles;

    @Valid
    private UserBaseExt01DTO ext01;

    private UserBaseExt02DTO ext02;

}
