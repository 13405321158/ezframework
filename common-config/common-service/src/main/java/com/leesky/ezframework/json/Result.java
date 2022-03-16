/*
  @author 魏来
 * @data:Nov 9, 20192:58:33 PM
 * @Department Domestic Sales, Tech Center
 * @Desc: <li>控制器返回值包装类型
 */
package com.leesky.ezframework.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.utils.RsaToolUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings({"unchecked", "rawtypes"})
public class Result<T> {
    private Integer code = 200;//有些前端框架 读取返回值需要这个标志位
    private T data;// 返回数据
    private Long count;//记录总数
    private String msg;// 提示信息
    private boolean success;// 是否成功
    private static Boolean rsa;// 返回到前端的数据是否加密
    private Map<String, Object> attributes;// 其他参数

    private Result(T data) {
        this.data = data;
    }

    private Result(T data, Long count) {
        this.data = data;
        this.count = count;
    }

    private Result(String msg, boolean b) {
        this.success = b;
        this.msg = StringUtils.isBlank(msg) ? "操作失败!" : msg;
    }

    public static <T> Result<T> success() {
        return new Result<>().ok();
    }

    public static <T> Result<T> success(String msg) {

        return new Result<>(msg, true);
    }

    public static <T> Result<T> success(T data, Boolean encrypt) {

        if (encrypt) {
            rsa = true;
            data = (T) RsaToolUtil.encryptByPrivateKey(JSON.toJSONString(data), Common.RSA_PRIVATE2048);
        }
        Result result = new Result<>(data).ok();
        if (data instanceof List)
            result.setCount((long) ((List) data).size());

        return result;
    }

    public static <T> Result<T> success(T data, Long total, Boolean encrypt) {
        if (encrypt) {
            rsa = true;
            data = (T) RsaToolUtil.encryptByPrivateKey(JSON.toJSONString(data), Common.RSA_PRIVATE2048);
        }
        return new Result<>(data, total).ok();
    }


    public static <T> Result<T> failed(String msg) {
        return new Result<>(msg, false);
    }


    private Result ok() {
        this.success = true;
        this.msg = StringUtils.isNotBlank(msg) ? msg : "操作成功";
        return this;
    }

}
