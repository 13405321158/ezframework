package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author weilai
 * @data 2018年10月30日 上午10:33:21
 *
 * @desc 类描述
 *       <li>客户端详情表
 */

@Getter
@Setter
@TableName("oauth_client_details")
public class OauthClientDetailsModel implements Serializable {

	private static final long serialVersionUID = -6399970297950884142L;

	@TableId
	private String clientId;// 用于唯一标识每一个客户端(client)；注册时必须填写(也可以服务端自动生成)，这个字段是必须的，实际应用也有叫app_key。
							// 即UserBaseModel 中的 username,本工程对其加密了，方法是EncrypMD5Util.eccrypt(username)

	private String resourceIds;// 客户端能访问的资源id集合，注册客户端时，根据实际需要可选择资源id，也可以根据不同的额注册流程，赋予对应的额资源id

	private String clientSecret;// 注册填写或者服务端自动生成，实际应用也有叫app_secret, 必须要有前缀代表加密方式：即UserBaseModel
								// 中的password经过BCryptPasswordEncoder加密，并且增加前缀{bcrypt}

	private String scope;// 指定client的权限范围，比如读写权限，比如移动端还是web端权限

	private String authorizedGrantTypes;// 可选值 授权码模式:authorization_code,密码模式:password,刷新token: refresh_token, 隐式模式:
										// implicit: 客户端模式: client_credentials。支持多个用逗号分隔

	private String webServerRedirectUri;// 客户端重定向uri，authorization_code和implicit需要该值进行校验，注册时填写，

	private String authorities;// 指定用户的权限范围，如果授权的过程需要用户登陆，该字段不生效，implicit和client_credentials需要

	private Integer accessTokenValidity;// 设置access_token的有效时间(秒),默认(606012,12小时)

	private Integer refreshTokenValidity;// 设置refresh_token有效期(秒)，默认(606024*30, 30填)

	private String additionalInformation;// 值必须是json格式{"key", "value"}

	private String autoapprove;// 默认false,适用于authorization_code模式,设置用户是否自动approval操作,设置true跳过用户确认授权操作页面，直接跳到redirect_uri

	private Date createDate = new Date();

	private Date modifyDate = new Date();

	public OauthClientDetailsModel() {

	}

	public OauthClientDetailsModel(String clientId, String clientSecret) {
		this.scope = "all";
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.authorizedGrantTypes = "password,refresh_token";
		this.modifyDate = new Date();

	}

}
