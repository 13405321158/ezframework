/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午1:50
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model.buyer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("cbm_mag_buy_user")
@ApiModel(value = "买家(会员)基本信息")
public class BuyerBaseModel extends BaseUuidModel {

    private static final long serialVersionUID = -2026920241697117408L;
    private String username;

    private String mobile;
}
