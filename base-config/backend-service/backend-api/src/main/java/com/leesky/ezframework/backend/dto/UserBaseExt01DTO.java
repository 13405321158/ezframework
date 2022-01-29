/*
 * @作者: 魏来
 * @日期: 2022/1/28 9:24
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBaseExt01DTO {

    private String id;

    private String idCard;//身份证号码

    private String idName;//身份证上的姓名
    @NotNull(message = "地址不能为空")
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


    private String userId;//用户id

}
