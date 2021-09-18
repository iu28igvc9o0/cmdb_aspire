package com.aspire.ums.cmdb.maintenance.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名称:
 * 类描述: 维保统计分析的请求类
 * 创建人:
 * 创建时间:
 * 版本:      v1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MaintenStatistAnalysisRequest {

    @JsonProperty("project_name")
    private String projectName;
    @JsonProperty("contract_produce")
    private String contractProduce;
    @JsonProperty("service_produce")
    private String serviceProduce;
    @JsonProperty("service_start_time")
    private String serviceStartTime;
    @JsonProperty("service_end_time")
    private String serviceEndTime;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("device_class")
    private String deviceClass;
    @JsonProperty("resource_pool")
    private String resourcePool;
    @JsonProperty("procure_type")
    private String procureType;
    @JsonProperty("service_type")
    private String serviceType;
    @JsonProperty("maintenance_project_type")
    private String maintenanceProjectType;
    @JsonProperty("device_type")
    private String deviceType;
    // 针对维保到期时间
    @JsonProperty("warranty_date")
    private String warrantyDate;
    // 设备区域
    @JsonProperty("device_area")
    private String deviceArea;
}
