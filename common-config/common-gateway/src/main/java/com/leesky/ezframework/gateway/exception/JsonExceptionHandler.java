/*
 * @author: weilai
 * @Data:上午8:46:21,2019年12月17日
 * @Org:Sentury Co., ltd.
 * @Department:Domestic Sales, Tech Center
 * @Desc: <li>描述此类的作用
 */
package com.leesky.ezframework.gateway.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
public class JsonExceptionHandler implements ErrorWebExceptionHandler {


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 按照异常类型进行处理
        HttpStatus httpStatus = null;
        if (ex instanceof NotFoundException)
            httpStatus = HttpStatus.NOT_FOUND;
        else if (ex instanceof ResponseStatusException)
            httpStatus = ((ResponseStatusException) ex).getStatus();
        else
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        // 封装响应体,此body可修改为自己的jsonBody
        Map<String, Object> result = Maps.newHashMap();

        result.put("code", httpStatus.value());
        result.put("success", false);
        result.put("msg", "[网关层提示异常]:" + split(ex.getMessage()));
        result.put("httpStatus", httpStatus);
        result.put("Tips", "Sentury Tire Co., Ltd");
        result.put("timestamp", LocalDateTime.now().toString());

        result.put("body", JSON.toJSONString(result));

        // 错误记录
        ServerHttpRequest request = exchange.getRequest();
        log.error("[网关层提示异常]→请求路径:{},异常信息:{}", request.getPath(), ex.getMessage());
        // 参考AbstractErrorWebExceptionHandler
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(result);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest).switchIfEmpty(Mono.error(ex)).flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));

    }


    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private final ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        return ServerResponse.status((HttpStatus) result.get("httpStatus")).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result.get("body")));
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonExceptionHandler.this.viewResolvers;
        }
    }

    private String split(String str) {
        if (str.contains("503 SERVICE_UNAVAILABLE")) {
            str = StringUtils.replace(str, "503 SERVICE_UNAVAILABLE \"Unable to find instance for ", "");
            str = str.replace("\"", "");
            str = StringUtils.join(str, " 在nacos中未发现");
        }
        return str;
    }
}
