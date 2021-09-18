/**
 * @Title: KafkaEnviroment.java
 * @Package com.migu.tsg.microservice.logcontroller.kafka
 * @Description:  Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年7月27日 下午4:34:11
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant;

/**
 * @ClassName: KafkaEnviroment
 * @Description:
 * @author tsg-frank
 */
public interface Enviroment {
	/**
	 * kafka hosts
	 */
	String KAFKA_SERVER = "KAFKA_HOSTS";
	/**
	 * kafka log topic name
	 */
	String KAFKA_LOG_TOPIC_NAME = "KAFKA_TOPIC_NAME";
	/**
	 * zookeeper host for kafka
	 */
	String KAFKA_ZK_URL = "KAFKA_ZK_URL";
	/**
	 * kafka group name
	 */
	String KAFKA_GROUPNAME = "KAFKA_GROUPNAME";
	/**
	 * elasticsearch url
	 */
	String ELASTICSEARCH_URL = "ELASTICSEARCH_URL";
	/**
	 * redis host
	 */
	String REDIS_HOST = "REDIS_HOST";
	/**
	 * partition total number
	 */
	String PARTITION_TOTAL_NUMBER = "PARTITION_TOTAL_NUMBER";
	/**
	 * partition number per node
	 */
	String PARTITION_NUMBER_PER_NODE = "PARTITION_NUMBER_PER_NODE";
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
	//String INDEX_ALL = "_all";
	/**
	 * 系统日志索引格式 log- + 日期 如 log-20170908
	 */
	String LOG_INDEX = "log-{0}";
	/**
	 * 系统日志索引格式 log- + 日期 如 log-20170908
	 */
	String LOG_INDEX_PREFIX = "log-";
	/**
	 * 短信es索引格式 sms- + 日期 如 sms-20171221
	 */
	String SMS_INDEX_PREFIX = "sms-";
	/**
     * 业务日志索引格式 services- + 日期 如 services-20170908
     */
    String BIZ_INDEX_PREFIX = "services-";
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
	 * 短信类型
	 */
	String SMS_TYPE = "sms";
	/**
     * 事件日志类型
     */
    String EVENT_TYPE = "event";
    /**
     * 业务日志类型
     */
    String BIZ_LOG_TYPE = "services";
}
