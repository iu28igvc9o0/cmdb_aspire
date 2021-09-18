package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
* Dashboard数据对象
* Project Name:composite-api
* File Name:DashboardItem.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: DashboardItem <br/>
* date: 2017年10月9日 下午2:23:37 <br/>
* Dashboard数据对象
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class DashboardItem {
    private String display_name;
    private String uuid;
    private Long created_at;
    private String namespace;
    private Long updated_at;
    private String created_by;
    private String dashboard_name;
    // 将页面的监控指标 统计方式 分组 图表类型组装成一个json数组保存
    private Object metrics;
    private String privilege;
    private String orders;
    private List<String> resource_actions;
}
