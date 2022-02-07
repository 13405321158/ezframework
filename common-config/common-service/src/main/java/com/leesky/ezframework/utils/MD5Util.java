/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午9:36
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <li>描述:
 */
public class MD5Util {
    /**
     * <li>:md5加密
     *
     * @作者: 魏来
     * @日期: 2021/11/17  上午9:38
     **/
    public static String encrypt(String info) {

        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] bytes = md5.digest(info.getBytes("utf8"));
            StringBuilder ret = new StringBuilder(bytes.length << 1);
            for (int i = 0; i < bytes.length; i++) {
                ret.append(Character.forDigit((bytes[i] >> 4) & 0xf, 16));
                ret.append(Character.forDigit(bytes[i] & 0xf, 16));
            }
            return ret.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
