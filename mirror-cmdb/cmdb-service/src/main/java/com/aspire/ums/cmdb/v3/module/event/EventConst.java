package com.aspire.ums.cmdb.v3.module.event;

import com.aspire.ums.cmdb.config.KafkaProducerFactory;
import org.apache.kafka.clients.producer.Producer;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: EventConst
 * Author:   zhu.juwang
 * Date:     2020/5/13 9:28
 * Description: 配置项事件类型定义表
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class EventConst {
    // 模型数据新增事件
    public static final String EVENT_TYPE_DATA_INSERT = "data_insert";
    // 模型数据修改事件
    public static final String EVENT_TYPE_DATA_UPDATE = "data_update";
    // 模型数据删除事件
    public static final String EVENT_TYPE_DATA_DELETE = "data_delete";
    // 配置项数据新增事件
    public static final String EVENT_TYPE_CODE_INSERT = "code_insert";
    // 配置项数据修改事件
    public static final String EVENT_TYPE_CODE_UPDATE = "code_update";
    // 配置项数据删除事件
    public static final String EVENT_TYPE_CODE_DELETE = "code_delete";
    // 配置项数据变更事件
    public static final String EVENT_TYPE_CODE_CHANGE = "code_change";
    // 前端页面中配置项发生变更事件
    public static final String EVENT_TYPE_VUE_PAGE_CODE_CHANGE = "code_change";
    // 苏研key名（phyServers,virtualServers）
    public static final String SUYAN_TYPE_KEY_PREFIX = "suyan_type_key:%s";
    // 事件变更key
    public static final String CHANGE_EVENT_CODE = "change_event_code:%s_%s";
    // 需要被改变的模型id
    public static final String CHANGE_EVENT_MODULE = "change_event_module:%s";
    // Kafka生产器 key格式
    public static final String KAFKA_KEY_FORMAT = "%s_%s_%s";
    // 存储Kafka生产器
    public static final Map<String, KafkaProducerFactory> KAFAK_MAP = new HashMap<>();

}
