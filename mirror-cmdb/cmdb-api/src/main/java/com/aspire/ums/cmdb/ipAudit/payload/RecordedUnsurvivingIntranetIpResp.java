package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 存活未录入CMDB列表响应实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordedUnsurvivingIntranetIpResp extends IpAuditCommonResp {
    /**
     * IP类型：根据CMDB的类型，管理IP、业务IP1、业务IP2、IPV6、其他IP、consoleIP
     */
    private String ipType;
    /**
     * 主备
     */
    private String hostBackup;
    /**
     * 是否资源池管理
     */
    private String mgrByPool;
    /**
     * 所属独立业务
     */
    private String businessLevel1;
    /**
     * 独立业务子模块
     */
    private String businessLevel2;
    /**
     * 存活状态
     */
    private String surviveStatus;
    /**
     * 最近存活时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String lastSurviveTime;
    /**
     * 未存活时长(小时)
     */
    private String unsurvivedDuration;

    private String deviceStatus;
}
