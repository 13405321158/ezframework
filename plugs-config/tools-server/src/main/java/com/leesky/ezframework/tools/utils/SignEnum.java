package com.leesky.ezframework.tools.utils;

/**
 * 
 * @author weilai
 * @desc:
 *        <li>根据阿里云规定，发送短信必须使用阿里云平台审核通过的短信模板
 *        <li>以下枚举项 已经在阿里云平台审核通过
 */
public enum SignEnum {

	qlb("麒麟保", "SMS_190271362");

	private String sign;

	private String template;//模板内容请登录阿里云平台查看，需要新的模板请登录后定义、审核

	private SignEnum(String sign, String template) {
		this.sign = sign;
		this.template = template;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
