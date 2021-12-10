/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:00:54
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.backend.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 类功能说明：
 * <li></li>
 */
@Data
public class UserAuthDTO {
	

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态：1-有效；0-禁用
     */
    private Integer status;

    /**
     * 用户角色编码集合 ["ROOT","ADMIN"]
     */
    private List<String> roles= Lists.newArrayList("ROLE_rose01","role02");

}
