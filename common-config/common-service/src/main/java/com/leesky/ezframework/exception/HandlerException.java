package com.leesky.ezframework.exception;

import com.leesky.ezframework.json.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * desc：控制器抛出的异常捕获
 *
 * @author： 魏来
 * @date： 2021/12/10 下午6:02
 */
@Slf4j
@RestControllerAdvice
public class HandlerException {
    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Result.failed("不支持'" + e.getMethod() + "'请求");
    }

    /**
     * Exception 类异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
    /**
     * Exception 类异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class})
    public Result<?> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
    /**
     * BadSqlGrammarException 类异常信息(sql语句异常)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadSqlGrammarException.class})
    public Result<?> handleException(BadSqlGrammarException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getCause().getMessage());
    }
    /**
     * DuplicateKeyException 类异常信息(sql语句异常)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.failed("数据库中已存在该记录:");
    }

    /**
     * IllegalArgumentException 类异常信息（参数异常）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result<?> handleException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

}
