package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment.LOG_INDEX_PREFIX;
import static com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment.SMS_INDEX_PREFIX;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;

/**
 * 定义日志部分es的index和type规则 ，组件业务中不允许自定义 项目名称: 微服务运维平台（log-service 模块） 包:
 * com.migu.tsg.microservice.monitor.log.es 类名称: EsIndexManager.java 类描述:
 * 定义日志部分es的index和type规则 创建人: sunke 创建时间: 2017年9月1日 上午1:24:58
 */
public class EsIndexManager {

    private static final long TIME_MS_INTERVAL = 1000;
    private static final int BIZ_TIME_LENGTH = 13;

    /**
     * 查询同type是index名称
     * 
     * @return index名称
     */
    public static String getIndex() {
        return Enviroment.INDEX_ALL;
    }

    /**
     * 获取日志index名称
     * 
     * @return 日志index名称
     */
    public static String getLogIndex() {
        return genIndex(Enviroment.LOG_INDEX);
    }

    /**
     * 获取业务日志index名称
     * 
     * @return 日志业务index名称
     */
    public static String getBizIndex() {
        return genIndex(Enviroment.BIZ_LOG_INDEX);
    }

    /**
     * 获取事件index名称
     * 
     * @return 事件index名称
     */
    public static String getEventIndex() {
        return genIndex(Enviroment.EVENT_LOG_INDEX);
    }

    /**
     * 获取日志type名称
     * 
     * @return 日志type名称
     */
    public static String getLogType() {
        return Enviroment.LOG_TYPE;
    }

    /**
     * 获取短信类型
     * 
     * @return 短信类型
     */
    public static String getSmsType() {
        return Enviroment.SMS_TYPE;
    }

    /**
     * 获取事件type名称
     * 
     * @return 事件type名称
     */
    public static String getEventType() {
        return Enviroment.EVENT_TYPE;
    }

    /**
     * 获取业务日志type名称
     * 
     * @return 业务日志type名称
     */
    public static String getBizType() {
        return Enviroment.BIZ_LOG_TYPE;
    }

    /**
     * 根据模板生成index名称
     * 
     * @param template
     *            index 生成模板
     * @return index名称
     */
    public static String genIndex(String template) {
        return MessageFormat.format(template, DateUtils.getDateStr(new Date(), DateUtils.DATA_PATTERN_DATE_SYMBOL));
    }

    public static ArrayList<String> getMultiSmsIndex(Long start, Long end, String indexPrefix) {
        String prefix = indexPrefix;
        if (StringUtils.isEmpty(prefix)) {
            prefix = SMS_INDEX_PREFIX;
        }
        ArrayList<String> r = new ArrayList<>();
        for (Long l = start; l <= end; l = l + (24 * 60 * 60 * 1000)) {
            Date d = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            r.add(prefix + dateFormat.format(d));
        }
        if (r.size() == 0) {
            r.add(Enviroment.INDEX_ALL);
        } else {
            String endFile = prefix + System.currentTimeMillis();
            if (!r.contains(endFile)) {
                r.add(endFile);
            }
        }
        return r;
    }

    public static ArrayList<String> getMultiLogIndex(Long start, Long end, String indexPrefix) {
        if (StringUtils.isEmpty(indexPrefix)) {
            indexPrefix = LOG_INDEX_PREFIX;
        }
        Long start_time;
        Long end_time;
        ArrayList<String> r = new ArrayList<String>();
        if (StringUtils.isEmpty(indexPrefix)) {
            start_time = start / TIME_MS_INTERVAL;
            end_time = end / TIME_MS_INTERVAL;
        } else if (StringUtils.isNotEmpty(indexPrefix) && String.valueOf(start).length() == BIZ_TIME_LENGTH && String
                .valueOf(end).length() == BIZ_TIME_LENGTH) {
            start_time = start;
            end_time = end;
        } else {
            start_time = start / TIME_MS_INTERVAL;
            end_time = end / TIME_MS_INTERVAL;
        }

        for (Long l = start_time; l <= end_time; l = l + (24 * 60 * 60 * 1000)) {
            Date d = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            r.add(indexPrefix + dateFormat.format(d));
        }

        if (r.size() == 0) {
            r.add(Enviroment.INDEX_ALL);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String endFile = indexPrefix + dateFormat.format(new Date(end_time));
            if (!r.contains(endFile)) {
                r.add(endFile);
            }
        }
        return r;
    }
}
