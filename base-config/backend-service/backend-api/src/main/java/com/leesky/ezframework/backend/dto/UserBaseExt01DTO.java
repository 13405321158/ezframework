/*
 * @作者: 魏来
 * @日期: 2022/1/28 9:24
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.dto;

import com.leesky.ezframework.valid.ChinaName;
import com.leesky.ezframework.valid.ChinaStr;
import com.leesky.ezframework.valid.IdCard;
import com.leesky.ezframework.valid.Mobile;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserBaseExt01DTO {

    private String id;

    @IdCard
    private String idCard;//身份证号码

    @ChinaName
    private String idName;//身份证上的姓名

    private String idAddress;//身份证上的地址

    private String curAddress;//当前居住地址

    private String companyCode;//公司编码

    @ChinaStr
    private String companyName;//公司名称

    @Email
    private String email;//邮箱

    private String portrait;//头像

    private String idCardImg01;//身份证正面

    private String idCardImg02;//身份证反面

    private String contact;//紧急联系人

    @Mobile(message = "紧急联系人电话号码格式错误")
    private String contactTel;//紧急联系人电话


    private String userId;//用户id

}
