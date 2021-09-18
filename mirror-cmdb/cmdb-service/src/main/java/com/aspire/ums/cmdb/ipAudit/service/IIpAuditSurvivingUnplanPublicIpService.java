package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplanPublicIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;

/**
* ip稽核-存活未规划IP（公网IP） 业务逻辑接口
* @author fanwehui
* @date 2020-05-27 10:58:39
*/
public interface IIpAuditSurvivingUnplanPublicIpService extends BaseIpAuditService
        <IpAuditSurvivingUnplanPublicIp, IpAuditCommonResp, IpAuditCommonReq>{
}