package com.leesky.ezframework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: weilai
 * @Data:下午4:09:42,2020年2月1日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>驼峰命名字符串: 大写字母转小写并用下划线分割
 *        <li>例如：loginName ➡️ login_name
 */
public class Hump2underline {

	private static Pattern linePattern = Pattern.compile("_(\\w)");

	/**
	 * @ver: 1.0.0
	 * @author: weilai
	 * @date: 上午9:47:48,2020年1月31日
	 * @desc:
	 *        <li>驼峰命名字符串 转 下划线分割
	 */
	public static String build(String para) {
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;// 定位
		if (!para.contains("_")) {
			for (int i = 0; i < para.length(); i++) {
				if (Character.isUpperCase(para.charAt(i))) {
					sb.insert(i + temp, "_");
					temp += 1;
				}
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 
	 * @desc 下划线转驼峰
	 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
