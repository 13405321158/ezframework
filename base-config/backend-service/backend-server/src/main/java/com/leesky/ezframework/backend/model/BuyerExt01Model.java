/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午3:13
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.backend.mapper.IbuyerBaseMapper;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("user_buyer_ext01")
@ApiModel(value = "买家信息扩展表01")
public class BuyerExt01Model extends BaseUuidModel {

	private static final long serialVersionUID = 4876391008588060464L;

	@ApiModelProperty("身份证号码")
	private String idCard;

	@ApiModelProperty("所在省份")
	private String province;

	@ApiModelProperty("所在省份编码")
	private String provinceCode;

	@ApiModelProperty("所在城市")
	private String city;

	@ApiModelProperty("所在城市编码")
	private String cityCode;

	@ApiModelProperty("所在区县")
	private String town;

	@ApiModelProperty("所在区县编码")
	private String townCode;


	@ApiModelProperty("用户id")
	private String userId;

	@OneToOne
	@TableField(exist = false)
	@JoinColumn(name = "user_id")
	@EntityMapper(targetMapper = IbuyerBaseMapper.class, entityClass = BuyerBaseModel.class)
	private BuyerBaseModel userBaseModel;

}
