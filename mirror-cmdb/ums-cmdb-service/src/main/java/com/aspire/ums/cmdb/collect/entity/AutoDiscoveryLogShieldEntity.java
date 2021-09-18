package com.aspire.ums.cmdb.collect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ShieldEntity
 * Author:   HANGFANG
 * Date:     2019/4/19 14:30
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoDiscoveryLogShieldEntity {
    private String id;
    private String ruleId;
    private String instanceName;
}
