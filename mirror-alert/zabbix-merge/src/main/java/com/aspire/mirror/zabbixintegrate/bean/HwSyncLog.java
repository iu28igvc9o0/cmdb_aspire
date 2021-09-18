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
public class HwSyncLog {
    private String fromTime;
    private String toTime;
    private Date execTime;
    private String execDuration;
    private String status = "success";
    private String content;
    private Date createTime;
    private String url;
    private String configType;
    private String  idcTypeTag;
    private int statusCode;
    
    public void setStatusFail() {
    	this.status = "fail";
    }
    
}
