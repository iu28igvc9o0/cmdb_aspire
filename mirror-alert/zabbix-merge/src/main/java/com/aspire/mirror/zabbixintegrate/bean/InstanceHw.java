package com.aspire.mirror.zabbixintegrate.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Http请求返回实体类
 * <p>
 * 项目名称: 微服务运维平台
 * 包:      com.migu.tsg.microservice.monitor.manage.api.dto
 * 类名称:   MetricHttpResponse.java
 * 类描述:   Http请求返回实体类
 * 创建人:   LiangJun
 * 创建时间: 2017-09-26 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceHw {
    private String id;
    private String status;
    private String idcType;
    private String extraSpecs;
    private String locales;
    private String privateIps;
    private String class_Name;
    private String nativeId;
    private String hostId;
    private String bizRegionId;
    private Integer monitor_status;//监控状态（1监控，0不监控）
    private Date update_time;
    private String instance_info;
    private int  tag_type;
    private String regionId;
    
    
    
}
