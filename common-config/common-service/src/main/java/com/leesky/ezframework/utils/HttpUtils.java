/*
 * @作者: 魏来
 * @日期: 2022/2/11 下午4:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/11 下午4:43
 */
public class HttpUtils {


    /**
     * 获取请求客户端的真实ip地址
     *
     * @param
     * @return ip地址
     */
    public static String getIpAddress() {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getIP(request);

    }

    /**
     * 获取请求客户端的真实ip地址
     *
     * @param request 请求对象
     * @return ip地址
     */

    private static String getIP(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if (!checkIP(ip))
            ip = request.getHeader("X-Real-IP");

        if (!checkIP(ip))
            ip = request.getHeader("http_client_ip");

        if (!checkIP(ip))
            ip = request.getRemoteAddr();

        if (!checkIP(ip))
            ip = request.getHeader("Proxy-Client-IP");

        if (!checkIP(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");

        if (!checkIP(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1)
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();

        return ip;
    }

    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip) || StringUtils.split(ip,".").length != 4)
            return false;

        return true;
    }
}
