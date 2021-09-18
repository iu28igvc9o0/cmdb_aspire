package com.aspire.mirror.alert.api.dto.dashboard;

import java.util.List;

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
public class AlertRepPanelItemDTO {
	@JsonProperty("id")
    private String id;

    /**告警过滤名称*/
    @JsonProperty("panel_id")
    private String panel_id;

    /** 创建人*/
    @JsonProperty("item_id")
    private String item_id;
    
    /**
                   修改人
     */
    @JsonProperty("report_type")
    private String report_type;

    /** 业务系统 */
    @JsonProperty("report_name")
    private String report_name;

    /** 监控指标/内容，关联触发器name */
    @JsonProperty("time_value")
    private Integer time_value;

    /** 监控对象 */
    @JsonProperty("time_unit")
    private String time_unit;
    
    private String report_unit;
    
    private Integer conversion_type;
    
    private Integer conversion_val;
     
    List<AlertRepPanelMoniterItemDTO> moniterItems;
    
}
