package com.aspire.cmdb.agent.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyPmInstance
 * Author:   zhu.juwang
 * Date:     2019/11/29 16:50
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyPmInstance {
    private String name;
    private String manageIp;
    private String os;
    private String osDetail;
    private String rpName;
    private String podName;
    private String model;
    private String firstOrg;
    private String secondaryOrg;
    private String businessName;
    private String manufacturer;
    private String status;
    private String osStorage;
    private String serialNo;
    private String resourceId;
    private String cpuType;
    private String cpuFrequency;
    private String hbaSpec;
    private String cpuSpec;
    private String cpuNum;
    private String memorySize;
    private String hostName;
    private String manageNetmask;
    private String impiNetmask;
    private String impiIp;
}
