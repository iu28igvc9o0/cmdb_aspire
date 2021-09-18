package com.aspire.mirror.alert.api.dto.dashboard;

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
public class AlertRepPanelMoniterItemDTO {
	@JsonProperty("id")
    private String id;

    /**告警过滤名称*/
    @JsonProperty("item_id")
    private String item_id;

    /** 创建人*/
    @JsonProperty("resource_device")
    private String resource_device;
    
    /**
                   修改人
     */
    @JsonProperty("moniter_item")
    private String moniter_item;

    /** 业务系统 */
    @JsonProperty("view_name")
    private String view_name;

    /** 监控指标/内容，关联触发器name */
    @JsonProperty("count_type")
    private String count_type;
    
    @JsonProperty("resource_type")
    private Integer resource_type;
    
    private String unit;
    
    private String mointer_index;

    private String resource_device_ipStr;
    //设备分类
    private String device_class;
    //设备类型
    private String device_type;
    //子类
    private String subclass;
    //监控项
    private String moniter_item_name;
    
  //监控子项
    private String sub_moniter_items;
    
    
}
