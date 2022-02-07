/*
 * @作者: 魏来
 * @日期: 2022/1/29 11:23
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述: 校验参数时 发现异常时 抛出此类
 */
package com.leesky.ezframework.exception;

public class ApiException extends RuntimeException{
    private static final long serialVersionUID = 5117822208071494655L;

    public ApiException(Object Obj) {
        super(Obj.toString());
    }
}
