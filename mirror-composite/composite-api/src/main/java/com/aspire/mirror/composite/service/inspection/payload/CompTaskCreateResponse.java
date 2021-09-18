package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务创建返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskCreateResponse.java
 * 类描述:    任务创建返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 15:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskCreateResponse implements Serializable {
    private static final long serialVersionUID = -6085499176875191289L;
    /** 任务名 */
    private String name;

    /** 模版ID */
    @JsonProperty("task_id")
    private String taskId;
}
