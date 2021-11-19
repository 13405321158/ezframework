/*
 * @作者: 魏来
 * @日期: 2021/11/18  上午8:50
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.dto;

import lombok.Data;

/**
 * <li>描述:
 */
@Data
public class UserBaseDTO {

    private String username;
    private String password;
    private String status;
}
