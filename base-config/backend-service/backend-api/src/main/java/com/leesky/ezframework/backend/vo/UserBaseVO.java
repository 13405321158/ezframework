/*
 * @作者: 魏来
 * @日期: 2022/1/29 8:44
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBaseVO {

    private String id;
    private String username;
    private String nickName;//昵称
    private String mobile;//手机号
    private String status;//账号状态
    private Date byTime; //账户有效期至
    private Date editPwdDate;//修改密码时间
    private String ext01Id;//扩展表01主键
    private String ext02Id;//扩展表02主键
    private String remake;//备注
    private String auditStep;//审核

    //ext01 信息
    private String idCard;//身份证号码
    private String idName;//身份证上的姓名
    private String idAddress;//身份证上的地址
    private String curAddress;//当前居住地址
    private String companyCode;//公司编码
    private String companyName;//公司名称
    private String email;//邮箱
    private String portrait;//头像
    private String idCardImg01;//身份证正面
    private String idCardImg02;//身份证反面
    private String contact;//紧急联系人
    private String contactTel;//紧急联系人电话


    //ext02信息
    private String pda;//是否有pda使用权限
    private String iosId;//苹果系统id
    private String wxOpenId;//微信openid
}
