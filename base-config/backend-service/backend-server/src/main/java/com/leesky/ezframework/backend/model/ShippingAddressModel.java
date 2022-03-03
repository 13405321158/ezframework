/*
 * @作者: 魏来
 * @日期: 2022/3/3 下午2:03
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:收货地址实体类
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("cbm_ship_address")
@ApiModel(value = "收货地址信息")
public class ShippingAddressModel extends BaseUuidModel {
    private static final long serialVersionUID = -4051312597481744149L;

    @ApiModelProperty("是否为默认收货地址")
    private String defaultAdd;

    @ApiModelProperty("收货人")
    private String name;

    @ApiModelProperty("联系电话")
    private String mobile;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("省份编码")
    private String provinceCode;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty("区县")
    private String town;

    @ApiModelProperty("区县编码")
    private String townCode;

    @ApiModelProperty("详细门牌号")
    private String detailed;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(province);
        sb.append(city);
        sb.append(town);
        sb.append(detailed).append(" ");
        sb.append(name).append("[");
        sb.append(mobile).append("]");

        return sb.toString();
    }
}
