package com.aspire.mirror.threshold.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 动态阈值模型扩展
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    StandardDynamicModelExt
 * 类描述:    动态阈值模型扩展
 * 创建人:    JinSu
 * 创建时间:  2020/11/5 14:22
 * 版本:      v1.0
 */
@Data
public class StandardDynamicModelExt {
    public static final String MODEL_TYPE_DH = "1";
    @JsonProperty("id")
    private Long id;

    @JsonProperty("model_ids")
    private String modelIds;

    @JsonProperty("source_type")
    private String sourceType;

    @JsonProperty("resource_id")
    private String resourceId;
    // 1
    @JsonProperty("model_type")
    private String modelType;

    @JsonProperty("model_status")
    private String modelStatus;

    @JsonProperty("model_fail_msg")
    private String modelFailMsg;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("last_update_time")
    private Date lastUpdateTime;

    @JsonProperty("zhixindu")
    private String zhixindu;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("model_content")
    private String modelContent;

    private List<StandardDynamicModel> dynamicModelList;
}
