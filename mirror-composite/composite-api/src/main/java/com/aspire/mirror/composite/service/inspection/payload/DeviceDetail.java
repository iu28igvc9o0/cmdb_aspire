package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    DeviceDetail.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/28 20:56
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class DeviceDetail {
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("device_name")
    private String deviceName;
}
