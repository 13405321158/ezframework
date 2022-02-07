package com.leesky.ezframework.enums;

import org.apache.commons.lang3.StringUtils;

public enum StatusEnum {


    ENABLE("0", "启用"), DISENABLE("1", "停用");

    private String key;
    private String value;

    private StatusEnum(String key, String value) {
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
        String ret = null;

        for (StatusEnum e : StatusEnum.values()) {

            if (StringUtils.equals(key, e.getKey()))
                ret = e.getValue();
        }

        return ret;
    }
}
