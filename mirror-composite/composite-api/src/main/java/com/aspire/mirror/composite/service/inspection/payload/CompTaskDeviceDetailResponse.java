package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 任务设备详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskDeviceDetailResponse.java
 * 类描述:    任务设备详情
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 16:39
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskDeviceDetailResponse implements Serializable {
//    /**
//     * 设备ID集合
//     */
//    private String devices;
//    /**
//     * 设备名称集合
//     */
//    @JsonProperty("device_names")
//    private String deviceNames;

    @JsonProperty("device_instance_list")
    private List<DeviceDetail> deviceInstanceList;
    /**
     * 模板Id
     */
    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("template_name")
    private String templateName;
}
