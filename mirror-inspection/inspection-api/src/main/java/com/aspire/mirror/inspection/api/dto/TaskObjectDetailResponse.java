package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 任务设备详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    TaskObjectDetailResponse.java
 * 类描述:    任务设备详情
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 16:20
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskObjectDetailResponse {
    /**
     * ID
     */
    @JsonProperty("task_object_id")
    private String taskObjectId;
    /**
     * 任务ID
     */
    @JsonProperty("task_id")
    private String taskId;
    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;
    /**
     * 设备ID
     */
    @JsonProperty("object_type")
    private String objectType;
    
    /**
     * 设备类型
     */
    @JsonProperty("object_id")
    private String objectId;
}
