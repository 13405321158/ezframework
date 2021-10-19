/*
  @author leesky
 * @data:Nov 9, 20192:58:33 PM
 * @Org:Sentury Co., ltd.
 * @Department Domestic Sales, Tech Center
 * @Desc: <li>控制器返回值包装类型
 */
package com.leesky.ezframework.json;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.constant.Global;
import com.leesky.ezframework.utils.RsaTool;

import lombok.Data;

@Data
public class AjaxJson {
	private Object data;// 返回数据
	private Long count = 0L;
	private Integer code = 0;
	private Boolean rsa = false;// 返回到前端的数据是否加密
	private String msg = "操作成功";// 提示信息
	private boolean success = true;// 是否成功
	private Map<String, Object> attributes;// 其他参数

	public AjaxJson() {

	}

	public AjaxJson(boolean success) {
		if (!success)
			this.msg = "操作失败!";
		this.success = success;

	}

	public void setData(Object data, Boolean sec) {
		if (sec) {
			this.rsa = true;
			this.data = RsaTool.encryptByPrivateKey(JSON.toJSONString(data), Global.RSA_PRIVATE);
		}
	}

	public void setSuccess(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public void setCount(Object count) {
		if (count instanceof Long)
			this.count = (Long) count;
		if (count instanceof Integer)
			this.count = Long.valueOf((Integer) count);
	}

}
