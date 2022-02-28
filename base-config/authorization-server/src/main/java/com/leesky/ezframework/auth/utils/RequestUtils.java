package com.leesky.ezframework.auth.utils;


import com.leesky.ezframework.global.Common;
import com.leesky.ezframework.utils.Base64codeUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 */

public class RequestUtils {
    /**
     * 获取登录类型：password,sms_code,wechat,captcha
     *
     * @author: 魏来
     * @date: 2022/2/28 下午5:45
     */
    @SneakyThrows
    public static String getGrantType() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String grantType = request.getParameter(Common.GRANT_TYPE_KEY);
        return grantType;
    }

    /**
     * <li>SMS 或 图片验证码 方式登录时：从请求头获取验证码</li>
     *
     * @author: 魏来
     * @date: 2022/2/28 下午5:46
     */
    public static String getCode() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String code = request.getHeader(Common.CODE);
        Assert.isTrue(StringUtils.isNotBlank(code), "验证码不能是空值");
        return code;
    }

    /**
     * 获取登录认证的客户端ID
     * <p>
     * 兼容两种方式获取OAuth2客户端信息（client_id、client_secret）
     * 方式一：client_id、client_secret放在请求路径中
     * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密(BASE64算法)，例如 Basic Y2xpZW50OnNlY3JldA== （明文等于 client:secret)
     *
     * @return
     */
    @SneakyThrows
    public static String getOAuth2ClientId() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求路径中获取
        String clientId = request.getParameter(Common.CLIENT_ID_KEY);
        if (StringUtils.isNotBlank(clientId))
            return clientId;


        // 从请求头获取
        String basic = request.getHeader(Common.URL_HEADER_PARAM);
        if (StringUtils.isNotBlank(basic) && basic.startsWith(Common.BASIC)) {
            basic = basic.replace(Common.BASIC, Strings.EMPTY);
            String basicPlainText = Base64codeUtil.Base642String(basic);
            clientId = basicPlainText.split(":")[0]; //client:secret
        }
        return clientId;
    }


}
