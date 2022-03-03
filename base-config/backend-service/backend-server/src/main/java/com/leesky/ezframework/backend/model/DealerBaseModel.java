/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午1:50
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.IdealerExt01Mapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("user_dealer_base")
@ApiModel(value = "经销商用户基本信息")
public class DealerBaseModel extends ZbaseModel {

    private static final long serialVersionUID = 8989469045054554204L;

    @ApiModelProperty("公司编码")
    private String companyCode;

    @ApiModelProperty("公司名称")
    private String companyName;


    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("扩展表01主键")
    private String ext01Id;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext01_id")
    @EntityMapper(targetMapper = IdealerExt01Mapper.class, entityClass = DealerExt01Model.class)
    private DealerExt01Model ext01;



}
