/*
 * @作者: 魏来
 * @日期: 2021年12月3日  下午4:16:28
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.constant;

/**
 * 类功能说明：
 * <li>认证服务器在reids中使用的前缀</li>
 */
public class RedisGlobal {
	
	public static final String AUTH_TOKEN="auth-token:";//登录成功后 获取的token存储在这个地方

	public static final String AUTH_TOKEN_ID ="auth-token-id_";//登录用户id和token对应关系

}
