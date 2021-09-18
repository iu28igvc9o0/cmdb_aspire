package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;

import lombok.Data;

/**
 * 
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb.topo
 * 类名称:    PeerDeviceInfo.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/19 14:11
 * 版本:      v1.0
 */
@Data
public class RouteDeviceInfo {
	
    /**
     *端口
     */
    private String idcType;

    /**
     *优先级
     */
    private String ip;

    /**
     * 端口状态
     */
    private String status;
    /**
     *路径开销
     */
    private String code;
    
    int total;

    /**
     *根网桥
     */
    private String startTime;
    
    private String msg;
    
    private String deviceName;
    
    private String deviceDescription;
 

    private List<RouteDataInfo> routeDataList;


}
