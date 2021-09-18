package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnrecordIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpResp;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-05-21 11:20:10
*/
public interface IIpAuditSurvivingUnrecordIntranetIpService extends BaseIpAuditService
        <IpAuditSurvivingUnrecordIntranetIp, SurvivingUnrecordIntranetIpResp, SurvivingUnrecordIntranetIpReq> {
    public Map<String, Integer> getListCount(SurvivingUnrecordIntranetIpReq req, List<SurvivingUnrecordIntranetIpResp> list);
}