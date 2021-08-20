/*
  @author leesky
 * @data:Nov 9, 20192:58:33 PM
 * @Org:Sentury Co., ltd.
 * @Deparment Domestic Sales, Tech Center
 * @Desc: <li>控制器返回值包装类型
 */
package com.leesky.ezframework.json;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.constant.Global;
import com.leesky.ezframework.utils.RsaTool;
import lombok.Data;

import java.util.Map;



/**
 * @author leesky
 */
@Data
public class AjaxJson {

    private Integer code = 0;
    /**
     * 是否成功
     */
    private boolean success = true;
    /**
     * 提示信息
     */
    private String msg = "操作成功";
    /**
     * 返回数据
     */
    private Object data;

    private Long count = 0L;
    /**
     * 返回到前端的数据是否加密
     */
    private Boolean rsa = false;
    /**
     * 其他参数
     */
    private Map<String, Object> attributes;

    public AjaxJson() {

    }

    public AjaxJson(boolean success) {
        this.msg = "操作失败!";
        this.success = success;

    }

    public AjaxJson(boolean success, String msg) {
        super();
        this.msg = msg;
        this.success = success;

    }


    public void setMsg(String msg, String param) {
        this.msg = msg + param;
    }


    public void setData(Object data, Boolean sec) {
        if (sec) {
            this.rsa = true;
            this.data = RsaTool.encryptByPrivateKey(JSON.toJSONString(data), Global.RSA_PRIVATE);
        }
    }

    public void setSuccess(boolean success) {
        this.success = success;
        if (success) {
            return;
        }
        this.msg = "操作失败!";
    }

    public void setSuccess(boolean success, String msg) {
        this.success = success;

        this.msg = msg;

    }
}
