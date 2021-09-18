package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditRegistrationExpired;
import com.aspire.ums.cmdb.ipAudit.payload.*;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author huanggongrui
* @date 2020-05-22 14:33:58
*/
public interface IIpAuditRegistrationExpiredService extends BaseIpAuditService
        <IpAuditRegistrationExpired, RegistrationExpiredIpResp, RegistrationExpiredIpReq> {

    void genRegistrationExpiredIpData(String ipType);

    List<RegistrationExpiredPublicIpResp> listByEntity4PublicIp(RegistrationExpiredPublicIpReq req);

    int getListCount4PublicIp(RegistrationExpiredPublicIpReq data);

    Map<String, Object> getStatisticsData4PublicIp(RegistrationExpiredPublicIpReq req);

    List<RegistrationExpiredIpv6Resp> listByEntity4Ipv6(RegistrationExpiredIpv6Req req);

    int getListCount4Ipv6(RegistrationExpiredIpv6Req data);

    Map<String, Object> getStatisticsData4Ipv6(RegistrationExpiredIpv6Req req);


}