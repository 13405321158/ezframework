package com.leesky.ezframework.check;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RoleChecker {

    boolean check(Authentication authentication, HttpServletRequest request);

}
