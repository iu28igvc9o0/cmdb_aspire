package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplanIpV6;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;

import java.util.List;

/**
* ip稽核-存活规划IP（IPv6） 业务逻辑接口
* @author fanwehui
* @date 2020-05-27 10:58:39
*/
public interface IIpAuditSurvivingUnplanIpv6Service extends BaseIpAuditService
        <IpAuditSurvivingUnplanIpV6, IpAuditCommonResp, IpAuditCommonReq>{

    List<CmdbIpEntity> getAllIpv6List();
}