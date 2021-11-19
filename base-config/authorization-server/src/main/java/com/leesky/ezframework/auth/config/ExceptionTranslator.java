/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午8:44
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * <li>描述:
 */

@Component("exceptionTranslator")
public class ExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

        if (e instanceof InvalidGrantException)
            return handleOAuth2Exception(new UserLocker("账号已停用"));

        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }

        return new ResponseEntity<OAuth2Exception>(e, headers, HttpStatus.valueOf(status));

    }

    @SuppressWarnings("serial")
    private static class ServerErrorException extends OAuth2Exception {

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }

    }

    @SuppressWarnings("serial")
    private static class UserLocker extends OAuth2Exception {

        public UserLocker(String msg) {
            super(msg);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "User account is locked";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

    }
}

