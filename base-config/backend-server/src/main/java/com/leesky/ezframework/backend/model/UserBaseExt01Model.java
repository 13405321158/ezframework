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
import com.leesky.ezframework.join.interfaces.one2one.One2One;
import com.leesky.ezframework.model.BaseUuidModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_mag_user1_ext0")
@ApiModel(value = "基本用户扩展表01")
public class UserBaseExt01Model extends BaseUuidModel {

	private static final long serialVersionUID = -73195295975819428L;

	private String idCard = "370882197411031236";// 身份证号码：中文名称

	private String idName = "魏来";// 身份证上的姓名

	private String idAddress = "青岛市市北区湖光山色小区22-2-402";// 身份证上的地址

	private String companyName = "森麒麟(青岛)轮胎股份有限公司";// 所在公司名称

	private String nickName = "一个徘徊在牛A和牛C之间的程序员";// 昵称

	private String mobile = "13405321158";// 手机

	private String email = "13405321158@139.com";// 邮箱

	private String portrait;// 头像

	private String curAddress;// 当前地址：如外地员工在本市的临时住所

	private String contacts;// 紧急联系人

	private String contactsTel;// 紧急联系电话;

	private String dealerCode;// 经销商及其子用户时此字段 有效：经销商编码

	private String dealerName;// 经销商及其子用户时此字段 有效：经销商名称

	private String dealerCmId;// 门店用户时此字段有效上级经销商业务员

	private String ordersn = "0403a53c-5732-4b0a-aced-596b3b3d9320";

	@TableField(exist = false)
	@One2One(otherOneTableName = "cbm_mag_user1", joinColumn = "ext01_id")
	private UserBaseModel userBaseModel;
}
