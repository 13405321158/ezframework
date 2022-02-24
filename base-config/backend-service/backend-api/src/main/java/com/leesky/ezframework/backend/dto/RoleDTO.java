package com.leesky.ezframework.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leesky.ezframework.mybatis.model.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * desc：TODO
 *
 * @NotNull 验证对象是否不为null, 无法查检长度为0的字符串
 * @NotBlank 检查约束(字符串)是不是Null还有被Trim的长度是否大于0, 只对字符串, 且会去掉前后空格.
 * @NotEmpty 检查(集合)约束元素是否为NULL或者是EMPTY.
 * @author： 魏来
 * @date： 2021/12/10 下午1:15
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO extends BaseDTO {

    @NotBlank(message = "角色名称不能为空")
    private String name;//角色名称

    private String code;//角色代码,用在授权时使用 Role_

    @NotBlank(message = "角色描述不能为空")
    private String description;//角色描述

    private Integer sortNo;//排序

    @NotBlank(message = "角色类型不能为空")
    private String type;//角色类型：系统用户使用=sys001,会员用户使用=member001,经销商使用=经销商编码

    private Set<UserBaseDTO> users;
}
