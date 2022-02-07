/*
 * @作者: 魏来
 * @日期: 2021年8月25日  下午2:18:40
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Objects;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@TableName(value = "cbm_mag_group",autoResultMap = true)
@ApiModel(value = "群组信息表")
public class GroupModel extends BaseUuidModel {

	private static final long serialVersionUID = 682190171806572223L;

	@ApiModelProperty("群组名称")
	private String groupName;

	@TableField(exist = false)
//	@Many2Many(middleTableName = "cbm_mag_l_group_user", middleTableColumn = "group_id",otherMiddleTableColumn="user_id",otherTableName="cbm_mag_user")
	private Set<UserBaseModel> userSet;

	public GroupModel() {
//		this.groupName=RandomStringUtils.randomAlphanumeric(5);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GroupModel)) return false;
		GroupModel that = (GroupModel) o;
		return Objects.equal(getGroupName(), that.getGroupName()) && Objects.equal(getUserSet(), that.getUserSet());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getGroupName(), getUserSet());
	}
}
