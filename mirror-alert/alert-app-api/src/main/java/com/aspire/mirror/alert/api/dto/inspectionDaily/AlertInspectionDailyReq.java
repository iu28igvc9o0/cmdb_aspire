package com.aspire.mirror.alert.api.dto.inspectionDaily;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertInspectionDailyReq {
	 @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;


    
    /*资源池名称*/
    @JsonProperty("idcType")
    private String idcType;
    
    /*归属月份*/
    @JsonProperty("inspectionDate")
    private String inspectionDate;
    
    /*告警等级*/
    @JsonProperty("projectName")
    private String projectName;
    
    /*设备分类*/
    @JsonProperty("deviceType")
    private String deviceType;
    
    private String pod;
    
    private String deviceMfrs;
    
    private String deviceModel;
    
    private String deviceClass;
    
    private String cpuMoniter;
    
    private String memoryMoniter;
    
    private String order;
    
    private String sortName;
    
    @JsonProperty("inspectionDateStart")
    private String inspectionDateStart;
    
    @JsonProperty("inspectionDateEnd")
    private String inspectionDateEnd;
    
}
