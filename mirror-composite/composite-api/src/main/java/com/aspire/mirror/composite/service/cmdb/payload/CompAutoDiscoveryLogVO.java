package com.aspire.mirror.composite.service.cmdb.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompAutoDiscoveryLogVO
 * Author:   HANGFANG
 * Date:     2019/4/22 19:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompAutoDiscoveryLogVO {
    private String id;
    private String ruleId;
    private String instanceName;
    private String status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
