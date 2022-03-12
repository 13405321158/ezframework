/*
 * @作者: 魏来
 * @日期: 2022/3/12 下午1:19
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/3/12 下午1:19
 */
@Getter
@AllArgsConstructor
public enum ClientTypeEnum {


    PWD("password", "密码方式"),

    REFRESH("refresh_token", "刷新Token"),

    CAPTCHA("captcha", "图片验证码"),

    SMS("sms_code", "手机短信"),

    QR("wx_scan", "微信扫码"),

    ;

    /**
     * 获取token类型编码
     */
    private final String code;

    private final String name;



}
