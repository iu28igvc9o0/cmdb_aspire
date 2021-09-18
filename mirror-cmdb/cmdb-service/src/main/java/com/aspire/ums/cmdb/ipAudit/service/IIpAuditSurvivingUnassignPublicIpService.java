package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignPublicIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpResp;

/**
 * @author fanwenhui
 * @date 2020-05-28 20:15
 * @description ip稽核-存活未分配IP（公网IP） 业务逻辑接口
 */
public interface IIpAuditSurvivingUnassignPublicIpService extends BaseIpAuditService
        <IpAuditSurvivingUnassignPublicIp, UnassignPublicIpResp, UnassignPublicIpReq>{

    /**
     * 更新公网IP地址库和网段库相关信息
     * @param req 更新内容
     */
    void updatePublicInfo(IpAuditUpdateRequest req);
}
