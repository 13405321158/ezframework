/*
 * @作者: 魏来
 * @日期: 2021年9月3日  下午3:23:59
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.query;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class ConverterListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String MONTH_PATTERN = "yyyy-MM";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {//父容器为null就表示是root applicationContext


            ConvertUtils.register(new Converter() {
                @SuppressWarnings("unchecked")
				@Override
                public <T> T convert(Class<T> type, Object value) {

                    try {
                        Date date = DateUtils.parseDate((String) value, DATETIME_PATTERN, DATE_PATTERN, MONTH_PATTERN);
                        return (T) date;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }, Date.class);
        }
    }
}