/*
 * @作者: 魏来
 * @日期: 2021年11月19日  下午4:00:54
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.dto;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * 类功能说明：
 * <li></li>
 */
@Data
@NoArgsConstructor
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
    private String status;
    /*
     * 有效期至
     */
    private Date byTime;
    /*
     * 公司编码
     */
    private String companyCode;
    /*
     * 公司名称
     */
    private String companyName;
    /*
     * 头像
     */
    private String portrait;

    /**
     * 用户角色编码集合 ["ROOT","ADMIN"]
     */
    private Set<String> roles = Sets.newHashSet();

    public UserAuthDTO(String id, String username, String password, String status, Date byTime,String companyCode,String companyName,String portrait) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.byTime = byTime;

        this.companyCode=companyCode;
        this.companyName=companyName;
        this.portrait=portrait;
    }
}
