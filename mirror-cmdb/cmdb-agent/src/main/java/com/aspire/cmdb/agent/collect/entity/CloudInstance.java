package com.aspire.cmdb.agent.collect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudInstance
 * Author:   zhu.juwang
 * Date:     2019/11/28 13:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudInstance {
    private String instanceIp;
    private String idcType;
    private String deviceType;
}
