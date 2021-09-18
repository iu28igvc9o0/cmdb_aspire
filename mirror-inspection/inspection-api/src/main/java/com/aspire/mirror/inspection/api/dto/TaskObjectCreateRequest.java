package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    TaskObjectCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 13:39
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TaskObjectCreateRequest implements Serializable {
    private static final long serialVersionUID = -1270109253940060001L;
    /**
     * 任务ID
     */
    @NotBlank
    @JsonProperty("task_id")
    private String taskId;
    /**
     * 模板ID
     */
    @NotBlank
    @JsonProperty("template_id")
    private String templateId;
    
    /**
     * 设备类型
     */
    @NotBlank
    @JsonProperty("object_type")
    private String objectType;
    /**
     * 设备id或业务id
     */
    @NotBlank
    @JsonProperty("object_id")
    private String objectId;
    
   
    
    
}
