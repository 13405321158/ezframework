package com.leesky.ezframework.exception;

import com.leesky.ezframework.json.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * desc：通用异常捕获
 *
 * @author： 魏来
 * @date： 2021/12/10 下午6:02
 */
@Slf4j
@RestControllerAdvice
public class HandlerException {

    /**
     * Exception 类异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
}
