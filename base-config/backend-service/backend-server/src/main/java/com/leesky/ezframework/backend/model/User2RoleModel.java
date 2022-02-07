package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： 魏来
 * @date： 2021/12/10 下午1:27
 */
@Data
@NoArgsConstructor
@ApiModel(value = "用户角色关系表")
@TableName("cbm_mag_l_user_role")
public class User2RoleModel implements Serializable {

    private static final long serialVersionUID = -6662565190423185310L;
    @TableId(value = "id")
    @ApiModelProperty(value = "数据表主键")
    private String id;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("角色Id")
    private String roleId;

    public User2RoleModel(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
