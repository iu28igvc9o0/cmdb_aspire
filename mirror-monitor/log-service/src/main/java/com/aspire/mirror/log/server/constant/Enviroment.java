/**
 * @Title: KafkaEnviroment.java
 * @Package com.migu.tsg.microservice.logcontroller.kafka
 * @Description:  Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年7月27日 下午4:34:11
 * @version V1.0
 */

package com.aspire.mirror.log.server.constant;

/**
 * @ClassName: KafkaEnviroment
 * @Description:
 * @author tsg-frank
 */
public interface Enviroment {
    /**
     * bulk size
     */
    String BULK_SIZE = "BULK_SIZE";
    /**
     * log path
     */
    String LOG_PATH = "LOG_PATH";
    /**
     * agg label
     */
    String AGGLABEL = "group_by_label";
    /**
     * 查询所有索引
     */
    String INDEX_ALL = "_all";
    /**
     * 系统日志索引格式 log- + 日期 如 log-20170908
     */
    String LOG_INDEX = "log-{0}";
    /**
     * 业务日志索引格式 service- + 日期 如 services-20170908
     */
    String BIZ_LOG_INDEX = "services-{0}";
    /**
     * 事件日志索引格式 event- + 日期 如 event-20170908
     */
    String EVENT_LOG_INDEX = "event-{0}";
    /**
     * 系统日志类型
     */
    String LOG_TYPE = "log";
    /**
     * 事件日志类型
     */
    String EVENT_TYPE = "event";
    /**
     * 业务日志类型
     */
    String BIZ_LOG_TYPE = "services";
}
