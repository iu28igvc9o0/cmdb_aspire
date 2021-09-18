package com.aspire.mirror.alert.server.dao.dashboard.po;

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
public class AlertRepPanelMoniterItem {
	@JsonProperty("id")
    private String id;

    /**关联大屏配置项id*/
    @JsonProperty("item_id")
    private String item_id;

    /** 设备*/
    @JsonProperty("resource_device")
    private String resource_device;
    
    /**
           所选监控项       ：多项逗号分隔
     */
    @JsonProperty("moniter_item")
    private String moniter_item;
    //监控展示图表名称
    @JsonProperty("view_name")
    private String view_name;
//统计方式：avg、max、min
    /** 监控指标/内容，关联触发器name */
    @JsonProperty("count_type")
    private String count_type;
    //资源类型资源类型：1业务系统2资源池3机房4设备大类5设备小类6设备ip
    @JsonProperty("resource_type")
    private Integer resource_type;
    //单位
    private String unit;
    //索引
    private String mointer_index;
    //所选ip设备
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
