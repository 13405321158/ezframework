package com.leesky.ezframework.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/15 上午11:21
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {

    private String timestamp;// 发生时间

    private String path;// 访问Url

    private Integer code;// 错误类型

    private String msg; // 错误提示信息

    private String error_description;


    private Boolean success = false;// 状态位


    public String getMsg() {
        return StringUtils.isBlank(msg) ? this.error_description : msg;
    }

}
