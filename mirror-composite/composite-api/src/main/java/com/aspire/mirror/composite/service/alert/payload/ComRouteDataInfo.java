package com.aspire.mirror.composite.service.alert.payload;

import lombok.Data;

/**
 * TODO
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
public class ComRouteDataInfo {
	
    /**
     *路由协议
     */
    private String routeProto;

    /**
     *目的地址
     */
    private String routeDest;

    /**
     * 掩码
     */
    private String routeMask;
    /**
     *路由的下一跳地址
     */
    private String routeNextHop;

  
}
