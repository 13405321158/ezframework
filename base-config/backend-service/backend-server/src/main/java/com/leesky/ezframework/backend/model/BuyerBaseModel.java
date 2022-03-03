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
import com.leesky.ezframework.backend.mapper.IbuyerExt01Mapper;
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


@Getter
@Setter
@TableName("user_buyer_base")
@ApiModel(value = "买家基本信息")
public class BuyerBaseModel extends ZbaseModel {

    private static final long serialVersionUID = -2026920241697117408L;

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty("公司编码")
    private String companyCode;

    @ApiModelProperty("扩展表01主键")
    private String ext01Id;


    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "ext01_id")
    @EntityMapper(targetMapper = IbuyerExt01Mapper.class, entityClass = BuyerExt01Model.class)
    private BuyerExt01Model ext01;


    @OneToMany
    @JoinColumn
    @TableField(exist = false)
    @EntityMapper(targetMapper = IshippingAddressMapper.class, entityClass = ShippingAddressModel.class)
    private List<ShippingAddressModel> shipping;
}
