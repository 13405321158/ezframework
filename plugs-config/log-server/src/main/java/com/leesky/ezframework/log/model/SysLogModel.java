/**
 * @author weilai
 * @data 2018年11月21日 上午11:54:40
 * @desc 类描述
 * <li>日志实体类
 */
package com.leesky.ezframework.log.model;


import com.leesky.ezframework.dto.SysLogDTO;
import com.leesky.ezframework.nosql.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "cbm_mag_log")
public class SysLogModel extends BaseModel {

    @Field("user_name") // 用户名
    private String userName;

    @Field("user_id") // 用户id
    private String userId;

    @Field("company_name") // 用户所在公司
    private String companyName;

    @Field("id_name") // 用户名
    private String idName;

    @Field("module") // 第一级分类
    private String module;

    @Field("action") // 控制器方法
    private String action;

    @Field("method") // 请求方法
    private String method;

    @Field("params") // 请求参数
    private String params;

    @Field("ip") // IP地址
    private String ip;

    @Field("remake") // IP地址
    private String remake;//备注


    public SysLogModel(SysLogDTO dto) {
        LocalDateTime t = LocalDateTime.now();
        this.modifyDate= t;
        this.createDate = t;

        this.ip = dto.getIp();
        this.action = dto.getAction();
        this.module = dto.getModule();
        this.method = dto.getMethod();
        this.params = dto.getParams();
        this.remake = dto.getRemake();
        this.userId = dto.getUserId();
        this.userName = dto.getUserName();

    }

}
