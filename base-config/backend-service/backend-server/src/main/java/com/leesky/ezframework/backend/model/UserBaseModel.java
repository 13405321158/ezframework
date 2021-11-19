/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:08
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@TableName("cbm_mag_user")
@ApiModel(value = "基本用户信息")
public class UserBaseModel extends BaseUuidModel {

    private static final long serialVersionUID = 8547824568011487339L;
    @ApiModelProperty("登录名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("随机数")
    private String randomKey;

    @ApiModelProperty("修改密码时间")
    private Date editPwdDate;

    @ApiModelProperty("扩展表01主键")
    private String ext01Id;

    @ApiModelProperty("扩展表02主键")
    private String ext02Id;

    @TableField(exist = false)
//    @One2One(otherOneTableName = "cbm_mag_user_ext01", joinField = "ext01Id")
    private UserBaseExt01Model ext01;

    @TableField(exist = false)
//    @One2One(otherOneTableName = "cbm_mag_user_ext02", joinField = "ext02Id")
    private UserBaseExt02Model ext02;

    @TableField(exist = false)
//    @Many2Many(middleTableName = "cbm_mag_l_group_user", middleTableColumn = "user_id", otherMiddleTableColumn = "group_id", otherTableName = "cbm_mag_group")
    private Set<GroupModel> groupSet;

    public UserBaseModel() {
        build();
    }

    public UserBaseModel(UserBaseExt01Model ext01, UserBaseExt02Model ext02) {
        build();
        this.ext01 = ext01;
        this.ext02 = ext02;
    }

    public UserBaseModel(UserBaseExt01Model ext01, UserBaseExt02Model ext02, Set<GroupModel> group) {
        this.ext01 = ext01;
        this.ext02 = ext02;
        this.groupSet = group;
    }

    private void build() {
        this.username = RandomStringUtils.randomAlphanumeric(8);
        this.password = RandomStringUtils.randomAlphanumeric(4);
        this.randomKey = RandomStringUtils.randomAlphanumeric(4);
        this.editPwdDate = new Date();
    }
}
