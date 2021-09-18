package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author baiwp
 * @title: NetworkStrategyDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2716:11
 */
@Data
public class NetworkFillWallDto {
    /**
     * 防火墙IP
     */
    private String fillWallIp;
    /**
     * 策略编号
     */
    private String strategyNo;
    /**
     * 源区域
     */
    private String sourceRegion;
    /**
     * 目标区域
     */
    private String targetRegion;
    /**
     * 执行动作
     */
    private String executionAction;
    /**
     * 源地址
     */
    private String sourceAddr;
     /**
     * Snat地址
     */
    private String snatAddr;
    /**
     * 目的地址
     */
    private String targetAddr;
    /**
     * 目的内网IP
     */
    private String targetIntranetIp;
    /**
     * 服务端口
     */
    private String servicePort;
    /**
     * 所属机房
     */
    private String roomId;
    /**
     * 创建日期
     */
    private String createTime;
}
