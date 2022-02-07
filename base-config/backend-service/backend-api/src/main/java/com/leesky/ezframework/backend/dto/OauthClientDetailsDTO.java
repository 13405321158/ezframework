package com.leesky.ezframework.backend.dto;

import lombok.Data;


@Data
public class OauthClientDetailsDTO  {

	private String clientId;// 用于唯一标识每一个客户端(client)；注册时必须填写(也可以服务端自动生成)，这个字段是必须的，实际应用也有叫app_key。

	private String clientSecret;// 注册填写或者服务端自动生成，实际应用也有叫app_secret,

	private String scope;// 指定client的权限范围，比如读写权限，比如移动端还是web端权限

	private String resourceIds;

	private String authorizedGrantTypes;

	private String authorities;

	private String webServerRedirectUri;

	private Integer accessTokenValidity;

	private Integer refreshTokenValidity;
}
