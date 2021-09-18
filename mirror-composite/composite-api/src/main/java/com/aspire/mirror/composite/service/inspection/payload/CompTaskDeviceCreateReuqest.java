package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务设备请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskDeviceCreateReuqest.java
 * 类描述:    任务设备请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 10:28
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskDeviceCreateReuqest implements Serializable {
    /**
     * 设备ID集合，逗号分隔
     */
    private String devices;
    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;

}
