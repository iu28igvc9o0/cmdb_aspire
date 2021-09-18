package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb.topo
 * 类名称:    LldpData.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/19 14:09
 * 版本:      v1.0
 */
@Data
public class LldpData {
    /**
     * 本机端口描述
     */
    private String portDesc;

    /**
     * 本机端口
     */
    private String portIndex;

    /**
     *对端设备信息
     */
    private PeerDeviceInfo peerDeviveInfo;
}
