/*
 * @作者: 魏来
 * @日期: 2021年8月27日  下午2:23:17
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

import java.util.List;

@Getter
@Setter
@TableName("cbm_mag_order")
@ApiModel(value = "订单信息表")
public class DealerOrderModel extends BaseUuidModel {

	private static final long serialVersionUID = 1160360163040882996L;

	@ApiModelProperty("经销商名称")
	private String dealerName = "青岛东森轮胎贸易公司";

	@ApiModelProperty("经销商代码")
	private String dealerCode = "210026";

	@ApiModelProperty("订单编号")
	private String orderSn = "SO210"+RandomStringUtils.randomNumeric(5);

	@TableField(exist = false)
//	@One2Many(manyTableName = "cbm_mag_order_item", joinColumn = "order_num", joinField = "orderNum")
	private List<DealerOrderItemModel> items;

}
