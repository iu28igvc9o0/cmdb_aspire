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
public class ComStpDataInfo {
	
    /**
     *端口
     */
    private String stpPort;

    /**
     *优先级
     */
    private String stpPortPriority;

    /**
     * 端口状态
     */
    private String stpPortState;
    /**
     *路径开销
     */
    private String stpPortPathCost;

    /**
     *根网桥
     */
    private String stpPortDesignatedRoot;
    
    private String stpPortDesignatedCost;
    /**
     端口指定网桥
     */
    private String stpPortDesignatedBridge;
    /**
     * 设备指定端口
     */
    private String stpPortDesignatedPort;
    /**
    端口由学习状态向转发状态转变次数
     */
    private String stpPortForwardTransitions;
    
    private String stpPortPathCost32;
    
    private String ifOperStatus;
    /**
     * 端口索引
     */
    private String basePortIfIndex;
    
    private String ifDescr;
    /**
     * 端口的生成树协议使能或去使能状态
     */
    private String stpPortEnable;

  


}
