/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午2:18:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.model;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.join.interfaces.many2many.ManyToMany;
import com.leesky.ezframework.model.BaseUuidModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_mag_group1")
@ApiModel(value = "群组信息表")
public class GroupModel extends BaseUuidModel {

	private static final long serialVersionUID = 682190171806572223L;

	@ApiModelProperty("群组名称")
	private String groupName=RandomStringUtils.randomGraph(5);

	@TableField(exist = false)
	@ManyToMany(middleTableName = "cbm_mag_group_user", middleTableColumn = "group_id",otherMiddleTableColumn="user_id")
	private UserBaseModel userModel;
}
