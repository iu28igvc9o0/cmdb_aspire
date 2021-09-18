package com.aspire.cmdb.agent.ipscan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertIpEntity
 * Author:   zhu.juwang
 * Date:     2019/9/4 10:48
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertIpEntity {
    private String alertIp;
    private String alertStartTime;
    private String latestTime;
}
