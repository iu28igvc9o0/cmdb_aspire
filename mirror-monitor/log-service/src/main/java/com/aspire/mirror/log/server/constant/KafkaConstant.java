/**
 * @Title: KafkaConstant.java
 * @Package com.migu.tsg.microservice.atomicservice.log.kafka
 * @Description:  Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年7月28日 下午3:46:14
 * @version V1.0
 */

package com.aspire.mirror.log.server.constant;

/**
 * constant for kafka
 * @Description: Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @Package: com.aspire.mirror.log.server.constant
 * @Title: KafkaConstant.java
 * @version: V1.0.0.0
 * @author: sunke
 * @date: 2017/9/11 17:11
 */
public interface KafkaConstant {
    /***
     * 自动提交时间间隔（单位/毫秒）
     */
    String AUTO_COMMIT_INTERVAL_MS_DEFAULT = "600";

    /***
     * 每个topic分成kafkaStream的数量
     */
    int PARTITION_NUM_DEFAULT = 1;
    /***
     * 日志内容最大长度
     */
    int CONTENT_MAX_NUMBER = 2000;
    /***
     * 系统日志topic名称
     */
    String SYS_LOG_KAFKA_TOPIC = "system_log";

    /***
     * 业务日志topic名称
     */
    String BIZ_LOG_KAFKA_TOPIC = "biz_log";

    /***
     * 系统日志消费者id
     */
    String SYS_LOG_KAFKA_GROUPID = "group_system";

    /***
     * 业务日志消费者id
     */
    String BIZ_LOG_KAFKA_GROUPID = "group_biz";
}
