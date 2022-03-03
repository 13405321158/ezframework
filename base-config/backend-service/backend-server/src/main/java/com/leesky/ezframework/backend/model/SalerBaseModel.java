/*
 * @作者: 魏来
 * @日期: 2022/3/2 下午5:02
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.IsalerExt01Mapper;
import com.leesky.ezframework.backend.mapper.IshippingAddressMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/2 下午5:02
 */
@Getter
@Setter
@TableName("user_saler_base")
@ApiModel(value = "卖家基本信息")
public class SalerBaseModel extends ZbaseModel {
    private static final long serialVersionUID = -7296725719916650044L;

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
    @EntityMapper(targetMapper = IsalerExt01Mapper.class, entityClass = SalerExt01Model.class)
    private SalerExt01Model ext01;

    @OneToMany
    @JoinColumn
    @TableField(exist = false)
    @EntityMapper(targetMapper = IshippingAddressMapper.class, entityClass = ShippingAddressModel.class)
    private List<ShippingAddressModel> shipping;
}
