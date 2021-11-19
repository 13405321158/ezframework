package com.leesky.ezframework.auth.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author weilai
 * @data 2019年4月3日 下午3:13:15
 *
 * @desc 类描述
 *       <li>
 */
public class BcryptUtil {
	public static final String PASSWORD_TYPE_PREFIX = "{bcrypt}";

	private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	public static String getBcryptEncoder(String pwd) {

		String pwdEncode = bcryptEncoder.encode(pwd);

		return StringUtils.join(PASSWORD_TYPE_PREFIX, pwdEncode);

	}
	
	/**
	 * 加密
	 */
	public static String encode(String str ) {
		
		String salt = BCrypt.gensalt(16);
		
		return BCrypt.hashpw(str,salt);

	}
	
	/**
	 * 判断密码是否正确
	 * @desc plantext = 明文密码
	 * @desc hashed =加密后的密码
	 */
	public static Boolean disEncode(String plaintext,String hashed) {
		
		return BCrypt.checkpw(plaintext, hashed);

	}

}
