package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleKafkaEvent
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@AllArgsConstructor
public class CmdbModuleKafkaEvent {

    /**
     * ID
     */
    private String id;
    /**
     * 需要同步模型id
     */
    private String needSyncModuleId;
    /**
     * 事件类
     */
    private String kafkaHandlerClassId;
    /**
     * kafka topic
     */
    private String topic;
    /**
     * kafka 地址
     */
    private String kafkaAddress;
    /**
     * kafka 分区
     */
    private String partition;
}
