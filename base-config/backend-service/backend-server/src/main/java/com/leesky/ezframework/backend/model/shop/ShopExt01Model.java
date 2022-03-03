/*
 * @作者: 魏来
 * @日期: 2022/3/2 下午5:03
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model.shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.dealer.IdealerBaseMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/2 下午5:03
 */
@Getter
@Setter
@TableName("cbm_mag_member_ext01")
@ApiModel(value = "小B类用户基本信息：既是买家也是卖家")
public class ShopExt01Model extends BaseUuidModel {
    private static final long serialVersionUID = 4948845049505456943L;

    private String companyName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "user_id")
    @EntityMapper(targetMapper = IdealerBaseMapper.class, entityClass = ShopBaseModel.class)
    private ShopBaseModel userBaseModel;
}
