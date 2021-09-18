package com.aspire.cmdb.agent.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyncModuleDataInfo
 * Author:   hangfang
 * Date:     2021/1/27
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncModuleDataInfo {
    /**
     * 数据来源
     */
    @JsonProperty("data_from")
    private String dataFrom;
    /**
     * 源模型id
     */
    @JsonProperty("source_module")
    private String sourceModule;
    /**
     * 源字段
     */
    @JsonProperty("source_filed")
    private String sourceFiled;
    /**
     * 目标字段
     */
    @JsonProperty("target_filed")
    private String targetFiled;
    /**
     * 目标模型
     */
    @JsonProperty("target_module")
    private String targetModule;
    /**
     * 源级联字段
     */
    @JsonProperty("relation_filed")
    private String relationFiled;
    /**
     * 同步类型
     */
    @JsonProperty("sync_data_type")
    private String syncDataType;
    /**
     * 是否审核
     */
    @JsonProperty("enable_approve")
    private String enableApprove;
    /**
     * 目标字段
     */
    @JsonProperty("target_relation_filed")
    private String targetRelationFiled;
}
