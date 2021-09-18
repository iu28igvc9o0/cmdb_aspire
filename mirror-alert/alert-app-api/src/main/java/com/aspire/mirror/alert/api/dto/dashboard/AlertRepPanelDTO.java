package com.aspire.mirror.alert.api.dto.dashboard;

import java.util.Date;
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
public class AlertRepPanelDTO {
	@JsonProperty("id")
    private String id;

    /**告警过滤名称*/
    @JsonProperty("panel_name")
    private String panel_name;
    
    /**
                   修改人
     */
    @JsonProperty("show_item")
    private String show_item;

    /** 业务系统 */
    @JsonProperty("update_time")
    private Date update_time;

    /** 监控指标/内容，关联触发器name */
    @JsonProperty("create_time")
    private Date create_time;

    /** 监控对象 */
    @JsonProperty("creater")
    private String creater;
    
    @JsonProperty("editer")
    private String editer;
    //1私人2组内3全部
    private String access_type;
    
    //操作1copy2edit
    private String operateFlag;
    
    List<AlertRepPanelItemDTO> items;
}
