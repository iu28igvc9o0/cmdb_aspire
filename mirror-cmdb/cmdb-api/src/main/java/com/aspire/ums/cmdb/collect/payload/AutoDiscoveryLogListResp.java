package com.aspire.ums.cmdb.collect.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryLogEntity
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
public class AutoDiscoveryLogListResp {
    private String id;
    private String ruleId;
    private String instanceName;
    private String status;
    private String remark;
    private String createTime;
    private String updateTime;
    private String ruleName;
    private String discoveryType;
    private Integer collectCycle;
    private String cycleUnit;
}
