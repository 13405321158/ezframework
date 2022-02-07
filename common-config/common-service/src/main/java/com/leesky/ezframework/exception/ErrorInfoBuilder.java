package com.leesky.ezframework.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/15 上午11:21
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorInfoBuilder implements HandlerExceptionResolver, Ordered {


    private final static String ERROR_NAME = "error";

    private ErrorProperties errorProperties;

    public ErrorProperties getErrorProperties() {
        return errorProperties;
    }

    public void setErrorProperties(ErrorProperties errorProperties) {
        this.errorProperties = errorProperties;
    }

    /**
     * 错误构造器 (Constructor) 传递配置属性：server.xx -> server.error.xx
     */
    public ErrorInfoBuilder(ServerProperties serverProperties) {
        this.errorProperties = serverProperties.getError();
    }

    /**
     * 构建错误信息.(ErrorInfo)
     */
    public ErrorInfo getErrorInfo(HttpServletRequest request, HttpServletResponse response) {

        return getErrorInfo(request, response, getError(request));
    }

    /**
     * 构建错误信息.(ErrorInfo)
     */
    public ErrorInfo getErrorInfo(HttpServletRequest request, HttpServletResponse response, Throwable error) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setTimestamp(LocalDateTime.now().toString());
        errorInfo.setPath((String) request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE));
        errorInfo.setCode(response.getStatus());
        errorInfo.setMsg(error.getMessage());
        return errorInfo;
    }

    /**
     * 获取错误.(Error/Exception)

     * 获取方式：通过Request对象获取(Key="javax.servlet.error.exception").
     */
    public Throwable getError(HttpServletRequest request) {
        // 根据HandlerExceptionResolver接口方法来获取错误.
        Throwable error = (Throwable) request.getAttribute(ERROR_NAME);
        // 根据Request对象获取错误.
        if (error == null) {
            error = (Throwable) request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
        }
        // 当获取错误非空,取出RootCause.
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
        } // 当获取错误为null,此时我们设置错误信息即可.
        else {
            String message = (String) request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE);
            if (StringUtils.isEmpty(message)) {
                HttpStatus status = getHttpStatus(request);
                message = "Unknown Exception With " + status.value() + " " + status.getReasonPhrase();
            }
            error = new Exception(message);
        }
        return error;
    }

    /**
     * 获取通信状态(HttpStatus)
     */
    public HttpStatus getHttpStatus(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE);
        try {
            return statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * 获取堆栈轨迹(StackTrace)
     */
    public String getStackTraceInfo(Throwable error, boolean flag) {
        if (!flag) {
            return "omitted";
        }
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        return stackTrace.toString();
    }



    /**
     * 保存错误/异常.
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        request.setAttribute(ERROR_NAME, ex);
        return null;
    }

    /**
     * 提供优先级 或用于排序
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
