package com.leesky.ezframework.backend.enums;

/**
 * <li>登录方式枚举：密码、短信、微信等</li>
 * <li>不同登录方式 查询的字段不同</li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午3:18
 */
public enum LoginTypeEnum {
    password("u", "Q_username_EQ"), sms("s", "Q_mobile_EQ"), wx("w", "Q_openid_EQ");

    private String key;
    private String value;

    LoginTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(String key) {
        String str = null;
        for (LoginTypeEnum e : LoginTypeEnum.values()) {
            if (e.key.equals(key))
                return e.getValue();
        }
        return str;
    }
}
