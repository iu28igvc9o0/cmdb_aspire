package com.aspire.ums.cmdb.collect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryRuleEntity
 * Author:   zhu.juwang
 * Date:     2019/4/1 14:30
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoDiscoveryRuleEntity {
    private String id;
    private String ruleName;
    private String moduleId;
    private String discoveryType;
    private String discoveryParam;
    private String startScanIp;
    private String endScanIp;
    private String scanIp;
    private Integer collectCycle;
    private String cycleUnit;
    private String enable;
    private String room;
    private String createTime;
}
