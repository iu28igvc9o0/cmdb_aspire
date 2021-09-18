package com.aspire.ums.cmdb.ipAudit;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipAudit.payload.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @Author :  huangongrui
 * @Date :  2020/5/15 17:43
 */
@Api(value = "IP稽核报表接口类")
@RequestMapping("/cmdb/ipAudit")
public interface IIpAuditAPI {
    //============================存活未录入CMDB===================================
    @PostMapping(value = "/intranetIp/getSurvivingUnrecordIntranetIpList")
    @ApiOperation(value = "存活未录入CMDB-查询", notes = "存活未录入CMDB-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<SurvivingUnrecordIntranetIpResp> getSurvivingUnrecordIntranetIpList(@RequestBody SurvivingUnrecordIntranetIpReq req);

    @PostMapping(value = "/intranetIp/exportSurvivingUnrecordIntranetIpList")
    @ApiOperation(value = "存活未录入CMDB-导出", notes = "存活未录入CMDB-导出", tags = {"CMDB IPAUDIT API"})
    void exportSurvivingUnrecordIntranetIpList(SurvivingUnrecordIntranetIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateSurvivingUnrecordIntranetIpProcessStatus")
    @ApiOperation(value = "存活未录入CMDB-更新", notes = "存活未录入CMDB-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateSurvivingUnrecordIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================已录CMDB未存活IP=================================
    @PostMapping(value = "/intranetIp/getRecordedUnsurvivingIntranetIpList")
    @ApiOperation(value = "已录CMDB未存活IP-查询", notes = "已录CMDB未存活IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<RecordedUnsurvivingIntranetIpResp> getRecordedUnsurvivingIntranetIpList(@RequestBody RecordedUnsurvivingIntranetIpReq req);

    @PostMapping(value = "/intranetIp/exportRecordedUnsurvivingIntranetIpList")
    @ApiOperation(value = "已录CMDB未存活IP-导出", notes = "已录CMDB未存活IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportRecordedUnsurvivingIntranetIpList(RecordedUnsurvivingIntranetIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateRecordedUnsurvivingIntranetIpProcessStatus")
    @ApiOperation(value = "已录CMDB未存活IP-更新", notes = "已录CMDB未存活IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateRecordedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================已分配未存活IP=================================
    @PostMapping(value = "/intranetIp/getAssignedUnsurvivingIntranetIpList")
    @ApiOperation(value = "已分配未存活IP-查询", notes = "已分配未存活IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<AssignedUnsurvivingIntranetIpResp> getAssignedUnsurvivingIntranetIpList(
            @RequestBody AssignedUnsurvivingIntranetIpReq req);

    @PostMapping(value = "/intranetIp/exportAssignedUnsurvivingIntranetIpList")
    @ApiOperation(value = "已分配未存活IP-导出", notes = "已分配未存活IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportAssignedUnsurvivingIntranetIpList(@RequestBody AssignedUnsurvivingIntranetIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateAssignedUnsurvivingIntranetIpProcessStatus")
    @ApiOperation(value = "已分配未存活IP-更新", notes = "已分配未存活IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateAssignedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================存活未规划IP=================================
    @PostMapping(value = "/intranetIp/getSurvivingUnplannedIntranetIpList")
    @ApiOperation(value = "存活未规划IP-查询", notes = "存活未规划IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<SurvivingUnplannedIntranetIpResp> getSurvivingUnplannedIntranetIpList(
            @RequestBody SurvivingUnplannedIntranetIpReq req);

    @PostMapping(value = "/intranetIp/exportSurvivingUnplannedIntranetIpList")
    @ApiOperation(value = "存活未规划IP-导出", notes = "存活未规划IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportSurvivingUnplannedIntranetIpList(@RequestBody SurvivingUnplannedIntranetIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateSurvivingUnplannedIntranetIpProcessStatus")
    @ApiOperation(value = "存活未规划IP-更新", notes = "存活未规划IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateSurvivingUnplannedIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================存活未分配IP=================================
    @PostMapping(value = "/intranetIp/getSurvivingUnassignIntranetIpList")
    @ApiOperation(value = "存活未分配IP-查询", notes = "存活未分配IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<SurvivingUnassignIntranetIpResp> getSurvivingUnassignIntranetIpList(
            @RequestBody SurvivingUnassignIntranetIpReq req);

    @PostMapping(value = "/intranetIp/exportSurvivingUnassignIntranetIpList")
    @ApiOperation(value = "存活未分配IP-导出", notes = "存活未分配IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportSurvivingUnassignIntranetIpList(@RequestBody SurvivingUnassignIntranetIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateSurvivingUnassignIntranetIpProcessStatus")
    @ApiOperation(value = "存活未分配IP-更新", notes = "存活未分配IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateSurvivingUnassignIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================登记已过期IP=================================
    @PostMapping(value = "/getRegistrationExpiredIpList")
    @ApiOperation(value = "登记已过期IP-查询", notes = "登记已过期IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<RegistrationExpiredIpResp> getRegistrationExpiredIpList(
            @RequestBody RegistrationExpiredIpReq req);

    @PostMapping(value = "/exportRegistrationExpiredIpList")
    @ApiOperation(value = "登记已过期IP-导出", notes = "登记已过期IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportRegistrationExpiredIpList(@RequestBody RegistrationExpiredIpReq param, HttpServletResponse response);

    @PostMapping(value = "/updateRegistrationExpiredIpProcessStatus")
    @ApiOperation(value = "登记已过期IP-更新", notes = "登记已过期IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateRegistrationExpiredIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    //============================ =================================
    //============================ =================================
    //============================ =================================
    //============================ =================================
    //============================ =================================
    //============================ =================================
    //============================ =================================
    //============================ 公网IP =================================
    @PostMapping(value = "/intranetIp/generateAuditUnPlanData4PublicIp")
    @ApiOperation(value = "公网IP未规划对比结果录入", notes = "公网IP未规划对比结果录入", tags = {"CMDB IPAUDIT API"})
    void generateAuditUnPlanData4PublicIp();

    @PostMapping(value = "/intranetIp/generateAuditUnassignData4PublicIp")
    @ApiOperation(value = "公网IP未分配对比结果录入", notes = "公网IP未分配对比结果录入", tags = {"CMDB IPAUDIT API"})
    void generateAuditUnassignData4PublicIp();

    @PostMapping(value = "/intranetIp/getUnPlanPublicIpList")
    @ApiOperation(value = "存活未规划公网IP-查询", notes = "存活未规划公网IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<IpAuditCommonResp> getUnPlanPublicIpList(@RequestBody IpAuditCommonReq req);

    @PostMapping(value = "/intranetIp/exportUnPlanPublicIpList")
    @ApiOperation(value = "存活未规划公网IP-导出", notes = "存活未规划公网IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportUnPlanPublicIpList(@RequestBody IpAuditCommonReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/getUnAssignPublicIpList")
    @ApiOperation(value = "存活未分配公网IP-查询", notes = "存活未分配公网IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<UnassignPublicIpResp> getUnAssignPublicIpList(@RequestBody UnassignPublicIpReq req);

    @PostMapping(value = "/intranetIp/exportUnAssignPublicIpList")
    @ApiOperation(value = "存活未分配公网IP-导出", notes = "存活未分配公网IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportUnAssignPublicIpList(@RequestBody UnassignPublicIpReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updatePublicIpProcessStatus")
    @ApiOperation(value = "存活未规划/未分配公网IP-更新", notes = "存活未规划/未分配公网IP-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updatePublicIpProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/intranetIp/getRegistrationExpiredPublicIpList")
    @ApiOperation(value = "登记已过期公网IP-查询", notes = "登记已过期公网IP-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<RegistrationExpiredPublicIpResp> getRegistrationExpiredPublicIpList(
            @RequestBody RegistrationExpiredPublicIpReq req);

    @PostMapping(value = "/intranetIp/exportRegistrationExpiredPublicIpList")
    @ApiOperation(value = "登记已过期公网IP-导出", notes = "登记已过期公网IP-导出", tags = {"CMDB IPAUDIT API"})
    void exportRegistrationExpiredPublicIpList(@RequestBody RegistrationExpiredPublicIpReq param, HttpServletResponse response);

    //============================ IPv6 =================================
    @PostMapping(value = "/intranetIp/generateAuditUnPlanData4Ipv6")
    @ApiOperation(value = "IPv6未规划对比结果录入", notes = "IPv6未规划对比结果录入", tags = {"CMDB IPAUDIT API"})
    void generateAuditUnPlanData4Ipv6();

    @PostMapping(value = "/intranetIp/generateAuditUnassignData4Ipv6")
    @ApiOperation(value = "IPv6未分配对比结果录入", notes = "IPv6未分配对比结果录入", tags = {"CMDB IPAUDIT API"})
    void generateAuditUnassignData4Ipv6();

    @PostMapping(value = "/intranetIp/getUnPlanIPv6List")
    @ApiOperation(value = "存活未规划IPv6-查询", notes = "存活未规划IPv6-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<IpAuditCommonResp> getUnPlanIPv6List(@RequestBody IpAuditCommonReq req);

    @PostMapping(value = "/intranetIp/exportUnPlanIPv6List")
    @ApiOperation(value = "存活未规划IPv6-导出", notes = "存活未规划IPv6-导出", tags = {"CMDB IPAUDIT API"})
    void exportUnPlanIPv6List(@RequestBody IpAuditCommonReq param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/getUnAssignIPv6List")
    @ApiOperation(value = "存活未分配IPv6-查询", notes = "存活未分配IPv6-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<UnassignIpV6Resp> getUnAssignIPv6List(@RequestBody UnassignIpV6Req req);

    @PostMapping(value = "/intranetIp/exportUnAssignIPv6List")
    @ApiOperation(value = "存活未分配IPv6-导出", notes = "存活未分配IPv6-导出", tags = {"CMDB IPAUDIT API"})
    void exportUnAssignIPv6List(@RequestBody UnassignIpV6Req param, HttpServletResponse response);

    @PostMapping(value = "/intranetIp/updateIPv6ProcessStatus")
    @ApiOperation(value = "存活未规划/未分配IPv6-更新", notes = "存活未规划/未分配IPv6-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateIPv6ProcessStatus(@RequestBody IpAuditUpdateRequest request);

    @PostMapping(value = "/intranetIp/getRegistrationExpiredIpv6List")
    @ApiOperation(value = "登记已过期Ipv6-查询", notes = "登记已过期Ipv6-查询", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    IpAuditPageBean<RegistrationExpiredIpv6Resp> getRegistrationExpiredIpv6List(
            @RequestBody RegistrationExpiredIpv6Req req);

    @PostMapping(value = "/intranetIp/exportRegistrationExpiredIpv6List")
    @ApiOperation(value = "登记已过期Ipv6-导出", notes = "登记已过期Ipv6-导出", tags = {"CMDB IPAUDIT API"})
    void exportRegistrationExpiredIpv6List(@RequestBody RegistrationExpiredIpv6Req param, HttpServletResponse response);

    //==================下拉框数据值=======================
    @RequestMapping(value = "/getResource", method = RequestMethod.GET)
    @ApiOperation(value = "IP稽核下拉框数据接口", notes = "IP稽核下拉框数据接口", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo getResource(String type, String pid);

    //====================IP登记========================
    @PostMapping(value = "/updateIpRepositoryInfo")
    @ApiOperation(value = "IP登记-更新", notes = "IP登记-更新", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateIpRepositoryInfo(@RequestBody IpAuditUpdateRequest param);

    //====================IP稽核手动同步========================
    @GetMapping(value = "/doSync")
    @ApiOperation(value = "IP稽核手动同步", notes = "IP稽核手动同步", tags = {"CMDB IPAUDIT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "同步成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void doSync(@RequestParam String type);
}
