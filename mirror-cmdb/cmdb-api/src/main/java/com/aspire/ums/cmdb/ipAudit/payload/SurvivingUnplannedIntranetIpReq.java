package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 存活未规划IP列表请求实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
public class SurvivingUnplannedIntranetIpReq extends IpAuditCommonReq {
    private String auditSource;
}
