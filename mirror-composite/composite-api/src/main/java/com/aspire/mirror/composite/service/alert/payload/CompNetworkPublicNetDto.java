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
public class CompNetworkPublicNetDto {
    /**
     * 防火墙IP
     */
    private String fillWallIp;
    /**
     * 公网IP
     */
    private String publicNetIp;
    /**
     * 公网端口
     */
    private String publicNetPort;
    /**
     * 负载IP
     */
    private String loadIp;
    /**
     * 负载端口
     */
    private String loadPort;
    /**
     * 内网IP
     */
    private String targetIntranetIp;
    /**
     * 内网端口
     */
    private String targetIntranetPort;
    /**
     * 所属机房
     */
    private String roomId;
    /**
     * 创建日期
     */
    private String createTime;
}
