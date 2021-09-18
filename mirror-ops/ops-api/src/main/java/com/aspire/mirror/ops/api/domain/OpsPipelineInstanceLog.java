package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsPipelineInstanceLog.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/4/28 17:54
 * 版本:      v1.0
 */
@Data
public class OpsPipelineInstanceLog {
    @JsonProperty("pipeline_instance_id")
    private Long pipelineInstanceId;

    @JsonProperty("log_path")
    private String logPath;

    private Integer status;
}
