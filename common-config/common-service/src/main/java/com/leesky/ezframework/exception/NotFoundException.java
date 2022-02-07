package com.leesky.ezframework.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/15 上午10:53
 */

@RestController
@RequestMapping("${server.error.path:/error}")
public class NotFoundException implements ErrorController {


    /**
     * 错误信息的构建工具.
     */
    @Autowired
    private ErrorInfoBuilder errorInfoBuilder;

    /**
     * 错误信息页的路径
     */
    private final static String DEFAULT_ERROR_VIEW = "error";

    /**
     * 获取错误控制器的映射路径.
     */

    public String getErrorPath() {
        return errorInfoBuilder.getErrorProperties().getPath();
    }

    /**
     * 情况1：若预期返回类型为text/html,s则返回错误信息页(View).
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)

    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(DEFAULT_ERROR_VIEW, "errorInfo", errorInfoBuilder.getErrorInfo(request,response));
    }
    /**
     * 情况2：其它预期类型 则返回详细的错误信息(JSON).
     */
    @RequestMapping
    public ErrorInfo error(HttpServletRequest request, HttpServletResponse response) {
        return errorInfoBuilder.getErrorInfo(request,response);
    }
}