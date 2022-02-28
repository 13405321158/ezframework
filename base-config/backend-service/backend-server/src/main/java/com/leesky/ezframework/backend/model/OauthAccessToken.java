/**
 * @author: weilai
 * @Data:下午9:43:06,2019年11月23日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;


@Data
@ApiModel(value = "存放用户登录token(当前系统把token存放到redis中了)")
@TableName("oauth_access_token")
public class OauthAccessToken implements Serializable {

    private static final long serialVersionUID = 8342640841584747295L;

    @TableId(value = "token_id")
    private String tokenId;


    private Blob token;

    private String authenticationId;

    private String userName;

    private String clientId;

    private Blob authentication;

    private String refreshToken;

}
