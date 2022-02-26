package com.leesky.ezframework.tools.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/26 下午3:45
 */
public enum DoctypeEnmu {
    pdf("application/pdf"), png("image/jpg"), jpg("image/jpg"), gif("image/jif"), bmp("image/bmp");

    private final String key;

    DoctypeEnmu(String key) {
        this.key = key;
    }


    public static String getKey(String str) {

        String key = "image/jpg";
        for (DoctypeEnmu e : DoctypeEnmu.values()) {
            if (StringUtils.equals(e.name(), str))
                return e.key;
        }
        return key;
    }
}
