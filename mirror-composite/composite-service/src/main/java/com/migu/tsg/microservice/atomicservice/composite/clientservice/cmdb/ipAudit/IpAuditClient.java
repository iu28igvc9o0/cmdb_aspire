package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipAudit;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditPageBean;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredIpv6Req;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredIpv6Resp;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredPublicIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.RegistrationExpiredPublicIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Req;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Resp;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpResp;

/**
 * @Author: huanggongrui
 * @Description: IP稽核--内网ip相关报表接口
 * @Date: create in 2020/5/16 21:38
 */
@FeignClient(value = "CMDB")
public interface IpAuditClient {

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getSurvivingUnrecordIntranetIpList")
    IpAuditPageBean<SurvivingUnrecordIntranetIpResp> getSurvivingUnrecordIntranetIpList(
            @RequestBody SurvivingUnrecordIntranetIpReq survivingUnrecordIntranetIpReq);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateSurvivingUnrecordIntranetIpProcessStatus")
    ResultVo updateSurvivingUnrecordIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getRecordedUnsurvivingIntranetIpList")
    IpAuditPageBean<RecordedUnsurvivingIntranetIpResp> getRecordedUnsurvivingIntranetIpList(
            @RequestBody RecordedUnsurvivingIntranetIpReq recordedUnsurvivingIntranetIpReq);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateRecordedUnsurvivingIntranetIpProcessStatus")
    ResultVo updateRecordedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getAssignedUnsurvivingIntranetIpList")
    IpAuditPageBean<AssignedUnsurvivingIntranetIpResp> getAssignedUnsurvivingIntranetIpList(
            @RequestBody AssignedUnsurvivingIntranetIpReq assignedUnsurvivingIntranetIpReq);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateAssignedUnsurvivingIntranetIpProcessStatus")
    ResultVo updateAssignedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getSurvivingUnplannedIntranetIpList")
    IpAuditPageBean<SurvivingUnplannedIntranetIpResp> getSurvivingUnplannedIntranetIpList(
            @RequestBody SurvivingUnplannedIntranetIpReq survivingUnplannedIntranetIpReq);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateSurvivingUnplannedIntranetIpProcessStatus")
    ResultVo updateSurvivingUnplannedIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getSurvivingUnassignIntranetIpList")
    IpAuditPageBean<SurvivingUnassignIntranetIpResp> getSurvivingUnassignIntranetIpList(
            @RequestBody SurvivingUnassignIntranetIpReq survivingUnassignIntranetIpReq);

    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateSurvivingUnassignIntranetIpProcessStatus")
    ResultVo updateSurvivingUnassignIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/cmdb/ipAudit/getRegistrationExpiredIpList")
    IpAuditPageBean<RegistrationExpiredIpResp> getRegistrationExpiredIpList(
            @RequestBody RegistrationExpiredIpReq registrationExpiredIpReq);

    @PostMapping(value = "/cmdb/ipAudit/updateRegistrationExpiredIpProcessStatus")
    ResultVo updateRegistrationExpiredIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //=========================公网IP=============================
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getUnPlanPublicIpList")
    IpAuditPageBean<IpAuditCommonResp> getUnPlanPublicIpList(@RequestBody IpAuditCommonReq req);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getUnAssignPublicIpList")
    IpAuditPageBean<UnassignPublicIpResp> getUnAssignPublicIpList(@RequestBody UnassignPublicIpReq req);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updatePublicIpProcessStatus")
    ResultVo updatePublicIpProcessStatus(@RequestBody IpAuditUpdateRequest request);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getRegistrationExpiredPublicIpList")
    IpAuditPageBean<RegistrationExpiredPublicIpResp> getRegistrationExpiredPublicIpList(@RequestBody RegistrationExpiredPublicIpReq req);

    //=========================IPv6===============================
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getUnPlanIPv6List")
    IpAuditPageBean<IpAuditCommonResp> getUnPlanIPv6List(@RequestBody IpAuditCommonReq req);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getUnAssignIPv6List")
    IpAuditPageBean<UnassignIpV6Resp> getUnAssignIPv6List(@RequestBody UnassignIpV6Req req);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/updateIPv6ProcessStatus")
    ResultVo updateIPv6ProcessStatus(@RequestBody IpAuditUpdateRequest request);
    @PostMapping(value = "/cmdb/ipAudit/intranetIp/getRegistrationExpiredIpv6List")
    IpAuditPageBean<RegistrationExpiredIpv6Resp> getRegistrationExpiredIpv6List(@RequestBody RegistrationExpiredIpv6Req req);

    //====================下拉框数据==========================
    @GetMapping(value = "/cmdb/ipAudit/getResource")
    ResultVo getResource(@RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "pid", required = false) String pid);

    @PostMapping(value = "/cmdb/ipAudit/updateIpRepositoryInfo")
    ResultVo updateIpRepositoryInfo(@RequestBody IpAuditUpdateRequest request);
}
