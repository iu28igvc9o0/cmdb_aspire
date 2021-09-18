package com.aspire.mirror.elasticsearch.api.dto;

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
public class PeerDeviceInfo {
    /**
     * 对端端口描述
     */
    private String peerPortDesc;

    /**
     *对端节点host
     */
    private String peerHost;

    /**
     * 对端节点hostname
     */
    private String peerHostname;
    /**
     *对端设备端口
     */
    private String peerPortIndex;

    /**
     *对端设备型号
     */
    private String peerDeviceModel;

    /**
     *对端设备类型
     */
    private String peerDeviceType;

    /**
     *对端设备品牌
     */
    private String peerDeviceBrand;


}
