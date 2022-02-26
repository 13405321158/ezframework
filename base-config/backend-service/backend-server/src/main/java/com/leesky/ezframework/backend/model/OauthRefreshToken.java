/**
 * @author: weilai
 * @Data:2020年5月14日下午3:14:12
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li> 刷新token
 */
package com.leesky.ezframework.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.ddl.annotation.ExcludeDDL;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;


@Data
@ExcludeDDL
@TableName("oauth_refresh_token")
public class OauthRefreshToken implements Serializable {
    private static final long serialVersionUID = 3745561592028594823L;

    @TableId(value = "token_id")
    private String tokenId;

    private Blob token;

    private Blob authentication;
}
