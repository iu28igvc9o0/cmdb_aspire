package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

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
public class AlertInspectionDaily {
	@JsonProperty("count")
    private Integer count;

    /**告警数量*/
    @JsonProperty("alertCount")
    private Integer alertCount;

    
    @JsonProperty("Count")
    private Integer moniterCount;
    
    private Integer impCount;
    
    private Integer highCount;
    
    private Integer middCount;
    
    private Integer lowCount;
    
    @JsonProperty("deviceType")
    private String deviceType;

    /** 厂家 */
    @JsonProperty("companeyName")
    private String companeyName;

    @JsonProperty("podName")
    private String podName;

    /** 期数*/
    @JsonProperty("projectName")
    private String projectName;
    
    @JsonProperty("deviceModel")
    private String deviceModel;
    
    @JsonProperty("deviceClass")
    private String deviceClass;
    
    /** 监控对象 */
    @JsonProperty("moniObject")
    private String moniObject;
    
    
}
