package com.leesky.ezframework.backend.dto;

import lombok.Data;

import java.util.Set;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/10 下午1:15
 */
@Data
public class RoleDTO {

    private String id;

    private String name;//角色名称

    private String code;//角色代码,用在授权时使用 Role_

    private String description;//角色描述

    private Integer sortNo;//排序

    private String type;//角色类型：系统用户使用=sys001,会员用户使用=member001,经销商使用=经销商编码

    private Set<UserBaseDTO> users;
}
