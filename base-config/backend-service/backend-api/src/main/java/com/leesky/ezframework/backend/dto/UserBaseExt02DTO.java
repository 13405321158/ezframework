/*
 * @作者: 魏来
 * @日期: 2022/1/28 9:25
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.dto;

import com.leesky.ezframework.mybatis.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserBaseExt02DTO extends BaseDTO {


    private String pda;

    private String iosId;

    private String wxOpenId;


}
