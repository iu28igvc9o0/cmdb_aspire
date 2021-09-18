/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.util <br>
* 类名称: InstantUtils.java <br>
* 类描述: 日期时间工具类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月12日下午4:21:42 <br>
* 版本: v1.0
*/
public class InstantUtils {

    /**
     * 获取当前时间的UTC格式的日期时间字符串
     * @return UTC格式的日期时间字符串
     */
    public static String now() {
        return formatter(Instant.now().atOffset(ZoneOffset.ofHours(8)).toString());
    }

    /**
     * 获取指定时间的UTC格式的日期时间字符串
     * @param timestamp 时间戳对象
     * @return UTC格式的日期时间字符串
     */
    public static String ofEpochMilli(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        // return formatter(timestamp.toInstant().atOffset(ZoneOffset.ofHours(8)).toString());
        // toInstant().toString()默认就是UTC格式：2017-11-01T13:22:26.80Z
        return timestamp.toInstant().toString();
    }

    /**
     * 获取指定时间的UTC格式的日期时间字符串
     * @param date 日期对象
     * @return UTC格式的日期时间字符串
     */
    public static String ofEpochMilli(final Date date) {
        if (date == null) {
            return null;
        }
        // return formatter(date.toInstant().atOffset(ZoneOffset.ofHours(8)).toString());
        return date.toInstant().toString();
    }

    /**
     * 格式化日期时间字符串
     * @param dateStr 日期时间字符串 2017-11-01T13:22:26.801+08:00
     * @return UTC格式的日期时间字符串
     */
    private static String formatter(final String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return StringUtils.substringBeforeLast(dateStr, "+") + "Z";
    }

}
