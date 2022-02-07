/**
 * @author weilai
 * @Data:2020-7-2310:06:41
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc: 
 *       <li>SHA-2加密散列函数系列由六个散列函数组成。这些是：

   <li> SHA-224，具有224位散列值
   <li> SHA-256，具有256位哈希值
   <li> SHA-384，具有384位哈希值
    <li>SHA-512，具有512位哈希值
    <li>SHA-512/224，具有512位散列值
    <li>SHA-512/256，具有512位哈希值

<li>其中，SHA-256和SHA-512是最常被接受和使用的散列函数，分别用32位和64位字计算。SHA-224和SHA-384分别是SHA-256和SHA-512的截断版本，使用不同的初始值计算。

<li>要在Java中计算加密散列值，请在java.security包下使用MessageDigest类。

<li>MessagDigest类提供以下加密哈希函数来查找文本的哈希值，如下所示：

    MD2
    MD5
    SHA-1
    SHA-224
    SHA-256
    SHA-384
    SHA-512

这些算法在名为getInstance（）的静态方法中初始化。选择算法后，计算消息摘要值，结果作为字节数组返回。使用BigInteger类将结果字节数组转换为其signum表示。然后将此表示转换为十六进制格式以获得预期的MessageDigest。
 */
package com.leesky.ezframework.gateway.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {

	/**
	 * 利用java原生的摘要实现SHA512加密
	 * 
	 * @param str 加密后的报文
	 * @desc 注意这个org.apache.commons.codec 比java原生的要好
	 */
	public static String getSHA256Str(String input) {
		String encdeStr = "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] hash = messageDigest.digest(input.getBytes("UTF-8"));
			encdeStr = Hex.encodeHexString(hash);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return encdeStr;

	}

	/**
	 * 
	 * 
	 * @Author:weilai
	 * @Data:2020-7-2310:16:42
	 * @Desc:
	 *        <li>签名比较
	 *        <li>plaintext =明文
	 *        <li>hashed=密文
	 *
	 */
	public static Boolean disEncode(String plaintext, String hashed) {
		String p = getSHA256Str(plaintext);
		return StringUtils.equals(p, hashed);

	}
}
