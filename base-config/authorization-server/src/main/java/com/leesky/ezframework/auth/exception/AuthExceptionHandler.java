/*
 * @作者: 魏来
 * @日期: 2021年12月2日  上午10:44:37
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.leesky.ezframework.json.AjaxJson;
/**
 * 类功能说明：
 * <li>全局异常捕获：拦截控制器抛出的异常，其中控制器方法中不能使用try cache捕获异常信息</li>
 */
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class AuthExceptionHandler {
	 /**
     * 用户不存在
     */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public AjaxJson handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new AjaxJson(false,"用户不存在");
    }

    /**
     * 用户名和密码异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidGrantException.class)
    public AjaxJson handleInvalidGrantException(InvalidGrantException e) {
    	  return new AjaxJson(false,"用户和密码不匹配");
    }


    /**
     * 账户异常(禁用、锁定、过期)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public AjaxJson handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
    	 return new AjaxJson(false,"账户异常(禁用、锁定、过期)");
    }

    /**
     * token 无效或已过期
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidTokenException.class})
    public AjaxJson handleInvalidTokenExceptionException(InvalidTokenException e) {
    	 return new AjaxJson(false,"无效或已过期");
    }


}
