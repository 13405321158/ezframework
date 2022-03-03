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
import com.leesky.ezframework.backend.mapper.IuserBaseExt01Mapper;
import com.leesky.ezframework.backend.mapper.IuserBaseExt02Mapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.ddl.annotation.ExcludeEdit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("user_sys_base")
@ApiModel(value = "平台用户基本信息")
public class UserBaseModel extends ZbaseModel {

    private static final long serialVersionUID = 3074985087761682444L;


    @ExcludeEdit
    @ApiModelProperty("扩展表01主键")
    private String ext01Id;

    @ExcludeEdit
    @ApiModelProperty("扩展表02主键")
    private String ext02Id;


    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext01_id")
    @EntityMapper(targetMapper = IuserBaseExt01Mapper.class, entityClass = UserBaseExt01Model.class)
    private UserBaseExt01Model ext01;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext02_id")
    @EntityMapper(targetMapper = IuserBaseExt02Mapper.class, entityClass = UserBaseExt02Model.class)
    private UserBaseExt02Model ext02;

}
