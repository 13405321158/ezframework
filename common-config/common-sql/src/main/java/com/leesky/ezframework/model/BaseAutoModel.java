/*
 * @作者: 魏来
 * @日期: 2021/7/29  上午9:07
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseAutoModel extends SuperModel implements Serializable {
	private static final long serialVersionUID = 7655518457578105527L;

	@ApiModelProperty(value = "数据表主键")
	@TableId(value = "id", type = IdType.AUTO)
	protected Long id;

}
