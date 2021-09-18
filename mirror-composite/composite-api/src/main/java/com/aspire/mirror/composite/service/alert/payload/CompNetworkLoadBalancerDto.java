package com.aspire.mirror.composite.service.alert.payload;

import lombok.Data;

/**
 * @author baiwp
 * @title: NetworkStrategyDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2716:11
 */
@Data
public class CompNetworkLoadBalancerDto {
    /**
     * 负载IP
     */
    private String loadIp;
    /**
     * 负载端口
     */
    private String loadPort;
    /**
     * 负载模式
     */
    private String loadMode;
    /**
     * 负载协议
     */
    private String loadProtocol;
    /**
     * 负载pool
     */
    private String loadPool;
    /**
     * 内网地址
     */
    private String IntranetAddr;
    /**
     * 内网端口
     */
    private String IntranetPort;
    /**
     * Snat类型
     */
    private String snatType;
    /**
     * 设备IP
     */
    private String deviceIp;
    /**
     *
     * 所属机房
     */
    private String roomId;
    /**
     * 创建日期
     */
    private String createTime;
}
