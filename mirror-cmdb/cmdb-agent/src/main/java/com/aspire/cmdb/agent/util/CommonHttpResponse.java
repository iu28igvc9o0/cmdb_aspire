package com.aspire.cmdb.agent.util;

import org.apache.http.HttpStatus;

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
public class CommonHttpResponse {
    private boolean responsed = true;
    private int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    private String content;
    public boolean isResponsed() {
        return responsed;
    }
    
 
    
    public void setResponsed(boolean responsed) {
        this.responsed = responsed;
    }
    
   
}
