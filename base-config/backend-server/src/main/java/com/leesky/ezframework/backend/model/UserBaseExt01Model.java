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
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
@TableName("cbm_mag_user_ext01")
@ApiModel(value = "基本用户扩展表01")
public class UserBaseExt01Model extends BaseUuidModel {

    private static final long serialVersionUID = -73195295975819428L;

    private String idCard;// 身份证号码：中文名称

    private String idName;// 身份证上的姓名

    private String idAddress;// 身份证上的地址

    private String companyName;// 所在公司名称

    private String nickName;// 昵称

    private String mobile;// 手机

    private String email;// 邮箱


    private String ordersn;

    @TableField(exist = false)
//    @One2One(otherOneTableName = "cbm_mag_user", otherOneTableColumn = "ext01_id")
    private UserBaseModel userBaseModel;

    public UserBaseExt01Model() {
        this.idCard = RandomStringUtils.randomAlphanumeric(21);
        this.idName = RandomStringUtils.randomAlphabetic(3);
        this.idAddress = RandomStringUtils.randomNumeric(3);
        this.companyName = "青岛森麒麟轮胎股份有限公司";
        this.nickName = RandomStringUtils.randomNumeric(9);
        this.mobile = "1340532" + RandomStringUtils.randomAlphanumeric(4);
        this.email = "1340532" + RandomStringUtils.randomAlphanumeric(4) + "@139.com";
        this.ordersn = RandomStringUtils.randomAlphanumeric(20);
    }
}
