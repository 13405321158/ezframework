package com.leesky.ezframework.global;

/**
 * 在redis中使用的常量或者 key前缀
 *
 * @author： 魏来
 * @date： 2021/12/14 下午12:38
 */
public class Redis {

    public static final String LOGIN_IMG_CODE = "validate_img_code_";//登录时图片验证码

    public static final String AUTH_TOKEN = "auth-token:";//登录成功后 获取的token存储在这个地方

    public static final String AUTH_TOKEN_ID = "auth-token-id_";//登录用户id和token对应关系


    public static final String URL_ROLES_KEY = "system:roles_rule:url:";

    public static final String BTN_ROLES_KEY = "system:roles_rule:btn:";

    public static final String SMS_KEY = "SMS_";

}
