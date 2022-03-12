package com.leesky.ezframework.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leesky.ezframework.backend.enums.ClientTypeEnum;
import com.leesky.ezframework.valid.Enum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OauthClientDetailsDTO {

    @NotEmpty
    private String clientId;// 用于唯一标识每一个客户端(client)；注册时必须填写(也可以服务端自动生成)，这个字段是必须的，实际应用也有叫app_key。
    @NotEmpty
    private String clientSecret;// 注册填写或者服务端自动生成，实际应用也有叫app_secret,

    private String scope;// 指定client的权限范围，比如读写权限，比如移动端还是web端权限

    private String resourceIds;

    @NotEmpty
    @Enum(value = ClientTypeEnum.class, message = "取值范围[password,refresh_token,captcha,sms_code,wx_scan]")
    private String authorizedGrantTypes;

    private String authorities;

    private String webServerRedirectUri;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;
}
