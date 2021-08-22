/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SuperModel implements Serializable {

	private static final long serialVersionUID = 3114932829247777386L;

	@TableField("create_date")
	@ApiModelProperty(value = "记录创建时间")
	protected Date createDate;


	@TableField("modify_date")
	@ApiModelProperty(value = "记录修改时间")
	protected Date modifyDate;

}
