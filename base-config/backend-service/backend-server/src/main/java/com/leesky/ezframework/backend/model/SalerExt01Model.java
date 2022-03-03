/*
 * @作者: 魏来
 * @日期: 2022/3/2 下午5:03
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.IdealerBaseMapper;
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
@TableName("user_saler_ext01")
@ApiModel(value = "卖家扩展信息01")
public class SalerExt01Model extends BaseUuidModel {
    private static final long serialVersionUID = 4948845049505456943L;




    @ApiModelProperty("用户id")
    private String userId;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "user_id")
    @EntityMapper(targetMapper = IdealerBaseMapper.class, entityClass = SalerBaseModel.class)
    private SalerBaseModel userBaseModel;
}
