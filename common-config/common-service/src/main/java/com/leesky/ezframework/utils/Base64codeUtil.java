package com.leesky.ezframework.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;


/**
 * @Ver: 1.0
 * @Author: weilai
 * @Date: 2018/9/21 11:25
 * @org: Leesky Institute of information technology
 * @desc <li>利用 Apache Commons Codec工具包生成 base64编码
 * <li>生成token时使用
 */
public class Base64codeUtil {

    // 编码
    public static String String2Base64(String str) {

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] codes = Base64.encodeBase64(bytes);

        return new String(codes);

    }

    // 解码
    public static String Base642String(String str) {

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] codes = Base64.decodeBase64(bytes);

        return new String(codes);

    }

    /**
     * @Author: weilai
     * @Date: 2018/9/21 11:26
     * @desc 用途:获取token时需要把username和 password编码为Base64，并增加前缀Basic
     */
    public static String getEncode(String username, String password) {
        String str = StringUtils.join(username, ":", password);
        str = String2Base64(str);
        return StringUtils.join("Basic ", str);
    }


}
