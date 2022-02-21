/*
 * @作者: 魏来
 * @日期: 2021年12月2日  上午10:44:37
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.leesky.ezframework.json.Result;

/**
 * 类功能说明：
 * <li>全局异常捕获：拦截控制器抛出的异常，其中控制器方法中不能使用try cache捕获异常信息</li>
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class AuthExceptionHandler {
    /**
     * 用户不存在
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Result handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    /**
     * 用户名和密码异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidGrantException.class)
    public Result handleInvalidGrantException(InvalidGrantException e) {
        log.error(e.getMessage(), e);
        return Result.failed("密码错误");
    }

    /**
     * 账户异常(禁用、锁定、过期)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public Result handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    /**
     * token 无效或已过期
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidTokenException.class})
    public Result handleInvalidTokenException(InvalidTokenException e) {
        log.error(e.getMessage(), e);
        return Result.failed("无效或已过期");
    }

    /**
     * 不支持的认证类型
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UnsupportedGrantTypeException.class})
    public Result handleException(UnsupportedGrantTypeException e) {
        log.error(e.getMessage(), e);
        return Result.failed("认证类型错误,参数在允许范围内");
    }

    /**
     * Exception 类异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
}
