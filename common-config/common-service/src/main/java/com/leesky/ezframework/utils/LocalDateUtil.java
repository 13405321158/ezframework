/*
 * @作者: 魏来
 * @日期: 2022/2/23 下午2:52
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.utils;

import java.time.*;
import java.util.Date;


public class LocalDateUtil {
    /**
     * LocalDate to Date
     *
     * @author: 魏来
     * @date: 2022/2/23 下午2:53
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime to Date
     *
     * @author: 魏来
     * @date: 2022/2/23 下午2:54
     */

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date  to  LocalDate
     *
     * @author: 魏来
     * @date: 2022/2/23 下午2:54
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date to LocalDateTime
     *
     * @author: 魏来
     * @date: 2022/2/23 下午2:54
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转时间戳（毫秒）
     *
     * @author: 魏来
     * @date: 2022/3/2 下午4:36
     */
    public static Long asEpochSecond(LocalDateTime time) {
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 时间戳 转 LocalDateTime
     *
     * @author: 魏来
     * @date: 2022/3/2 下午4:42
     */
    public static LocalDateTime asEpochSecond(Long time) {
        return LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8));
    }


}
