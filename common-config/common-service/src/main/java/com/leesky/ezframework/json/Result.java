/*
  @author 魏来
 * @data:Nov 9, 20192:58:33 PM
 * @Department Domestic Sales, Tech Center
 * @Desc: <li>控制器返回值包装类型
 */
package com.leesky.ezframework.json;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.utils.RsaTool;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor

@SuppressWarnings({"unchecked", "rawtypes"})
public class Result<T> {
    private T data;// 返回数据
    private Long count;
    private Integer code;
    private Boolean rsa;// 返回到前端的数据是否加密
    private String msg;// 提示信息
    private boolean success;// 是否成功
    private Map<String, Object> attributes;// 其他参数


    public Result(T data, Long count) {
        this.data = data;
        this.count = count;
    }


    public Result(String msg) {
        this.msg = msg;
    }


    public static <T> Result<T> success() {
        return new Result<>().ok();
    }

    public static <T> Result<T> success(T data, Boolean rsa) {
        T d = data;
        if (rsa)
            d = (T) RsaTool.encryptByPrivateKey(JSON.toJSONString(data), Common.RSA_PRIVATE);

        Result result = new Result<>(d).ok();
        if (data instanceof List) {
            result.setCount(Long.valueOf(((List) data).size()));
        }
        return result;
    }

    public static <T> Result<T> success(T data, Long total, Boolean rsa) {
        T d = data;
        if (rsa)
            d = (T) RsaTool.encryptByPrivateKey(JSON.toJSONString(data), Common.RSA_PRIVATE);

        return new Result<>(d, total).ok();
    }


    public static <T> Result<T> failed(String msg) {
        return new Result<>(msg).no();
    }


    public void setData(T data, Boolean sec) {
        if (sec) {
            this.rsa = true;
            this.data = (T) RsaTool.encryptByPrivateKey(JSON.toJSONString(data), Common.RSA_PRIVATE);
        }
    }

    private Result(T data) {
        this.data = data;
    }

    private Result ok() {
        this.code = 0;
        this.success = true;
        this.msg = StringUtils.isNotBlank(msg) ? msg : "操作成功";
        return this;
    }

    private Result no() {
        this.code = 0;
        this.success = false;
        this.msg = StringUtils.isNotBlank(msg) ? msg : "操作失败";
        return this;
    }
}
