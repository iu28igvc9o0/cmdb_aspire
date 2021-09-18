package com.aspire.mirror.composite.payload.monitorHttp;

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
 * 创建人:    longfeng
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class ComMonitorHttpReq {
	 @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;

    
    private String biz_system_id;
    
    private String idcType;
    
    private String monitor_name;
    
    private String request_http_addr;
    
    private Integer status;
    
    private Integer runStatus;
    
    private Integer configId;
}
