/**
 * @author weilai
 * @data 2019年1月31日 下午2:20:42
 * @desc 类描述
 * <li>
 */
package com.leesky.ezframework.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
public class I18nUtil {

    private static final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();


    /**
     * <li>返回国际化资源信息</li>
     *
     * @author: 魏来
     * @date: 2022/2/12 下午5:27
     */
    public String getMsg(String key) {
        return commonCode(key, null);
    }

    /**
     * <li>返回国际化资源信息：一个变量</li>
     *
     * @author: 魏来
     * @date: 2022/2/12 下午5:27
     */
    public String getMsg(String key, String p) {

        String[] param = new String[]{p};
        return commonCode(key, param);
    }

    /**
     * <li>返回国际化资源信息：两个变量</li>
     *
     * @author: 魏来
     * @date: 2022/2/12 下午5:27
     */
    public String getMsg(String key, String p1, String p2) {

        String[] param = new String[]{p1, p2};
        return commonCode(key, param);
    }

    /**
     * <li>返回国际化资源信息: 三个变量</li>
     *
     * @author: 魏来
     * @date: 2022/2/12 下午5:27
     */
    public String getMsg(String key, String p1, String p2, String p3) {
        String[] param = new String[]{p1, p2, p3};
        return commonCode(key, param);

    }


    private String commonCode(String key, String[] param) {
        String msg;
        try {
            messageSource.clearCache();
            messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
            messageSource.setBasenames("classpath:i18n/message");
            msg = messageSource.getMessage(key, param, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            msg = e.getMessage();
        }

        return msg;
    }
}
