/*
 * @作者: 魏来
 * @日期: 2021年8月27日  下午2:23:38
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.model;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.join.interfaces.many2one.Many2One;
import com.leesky.ezframework.model.BaseUuidModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "订单详情表")
@TableName("cbm_mag_order_item")

public class DealerOrderItemModel extends BaseUuidModel {

	private static final long serialVersionUID = -3828347204235792140L;

	@ApiModelProperty("物料编码")
	private String goodsCode = RandomStringUtils.randomNumeric(9);

	@ApiModelProperty("物料名称")
	private String goodsName = RandomStringUtils.randomAlphanumeric(10);

	@ApiModelProperty("订单编号")
	@TableField("order_num")
	private String orderNum;

	@TableField(exist = false)
	@Many2One(oneTableName = "cbm_mag_order", joinColumn = "order_sn", joinField = "orderSn")
	private DealerOrderModel orderinfo;
}
