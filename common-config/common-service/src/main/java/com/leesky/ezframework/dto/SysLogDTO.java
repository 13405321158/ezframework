/*
 * @作者: 魏来
 * @日期: 2022/2/11 下午4:41
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/11 下午4:41
 */
@Data
public class SysLogDTO implements Serializable {

    private static final long serialVersionUID = -6729304704766849262L;

    private String userId;

    private String userName;// 用户名

    private String module;// 第一级分类

    private String action;// 动作分类名称：注解名称

    private String method;// 请求方法

    private String params;// 请求参数

    private String ip;// IP地址

    private String remake;// 备注

    private Date createDate;
}
