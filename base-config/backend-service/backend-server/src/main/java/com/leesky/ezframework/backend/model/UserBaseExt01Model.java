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
import com.leesky.ezframework.backend.mapper.IuserBaseMapper;
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
@TableName("user_sys_ext01")
@ApiModel(value = "平台用户扩展信息01")
public class UserBaseExt01Model extends BaseUuidModel {

	private static final long serialVersionUID = 4876391008588060464L;

	@ApiModelProperty("身份证号码")
	private String idCard;

	@ApiModelProperty("身份证上的姓名")
	private String idName;

	@ApiModelProperty("身份证上的地址")
	private String idAddress;

	@ApiModelProperty("公司名称")
	private String companyName;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty("身份证正面")
	private String idCardImg01;

	@ApiModelProperty("身份证反面")
	private String idCardImg02;

	@ApiModelProperty("紧急联系人")
	private String contact;

	@ApiModelProperty("紧急联系人电话")
	private String contactTel;

	@ApiModelProperty("当前居住地址")
	private String curAddress;

	@ApiModelProperty("用户id")
	private String userId;

	@OneToOne
	@TableField(exist = false)
	@JoinColumn(name = "user_id")
	@EntityMapper(targetMapper = IuserBaseMapper.class, entityClass = UserBaseModel.class)
	private UserBaseModel userBaseModel;

}
