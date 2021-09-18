package com.aspire.cmdb.agent.ipAudit;

import com.aspire.ums.cmdb.ipAudit.service.*;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huanggongrui
 * @Description: IP稽核定时任务
 * @Date: create in 2020/5/25 15:06
 */
@Component
@Slf4j
//@EnableAsync
//@EnableScheduling
public class IpAuditSchedule {

    @Autowired
    private IIpAuditSurvivingUnrecordIntranetIpService survivingUnrecordIntranetIpService;
    @Autowired
    private IIpAuditRecordedUnsurvivingIntranetIpService recordedUnsurvivingIntranetIpService;
    @Autowired
    private IIpAuditAssignedUnsurvivingIntranetIpService assignedUnsurvivingIntranetIpService;
    @Autowired
    private IIpAuditSurvivingUnplannedIntranetIpService survivingUnplannedIntranetIpService;
    @Autowired
    private IIpAuditSurvivingUnassignIntranetIpService survivingUnassignIntranetIpService;
    @Autowired
    private IIpAuditRegistrationExpiredService registrationExpiredService;
    @Autowired
    private IIpAuditSurvivingUnplanPublicIpService survivingUnplanPublicIpService;
    @Autowired
    private IIpAuditSurvivingUnassignPublicIpService survivingUnassignPublicIpService;
    @Autowired
    private IIpAuditSurvivingUnassignIpV6Service survivingUnassignIpV6Service;
    @Autowired
    private IIpAuditSurvivingUnplanIpv6Service survivingUnplanIpv6Service;

    //-----------------------内网IP----------------------------
//    @Async
//    @Scheduled(cron = "0 2 3 * * ?") // 每天凌晨3点多触发
    @XxlJob("generateSurvivingUnrecordIntranetIpData")
    public ReturnT<String> generateSurvivingUnrecordIntranetIpData(String param) throws Exception{
        log.info("开始生成存活内网IP未录入CMDB数据...");
        survivingUnrecordIntranetIpService.generateAuditData();
        log.info("生成存活内网IP未录入CMDB数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 45 3 * * ?")
    @XxlJob("generateRecordedUnsurvivingIntranetIpData")
    public ReturnT<String> generateRecordedUnsurvivingIntranetIpData(String param) throws Exception{
        log.info("开始生成已录CMDB未存活内网IP数据...");
        recordedUnsurvivingIntranetIpService.generateAuditData();
        log.info("生成已录CMDB未存活内网IP数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 25 5 * * ?")
    @XxlJob("generateAssignedUnsurvivingIntranetIpData")
    public ReturnT<String> generateAssignedUnsurvivingIntranetIpData(String param) throws Exception{
        log.info("开始生成已分配未存活内网IP数据...");
        assignedUnsurvivingIntranetIpService.generateAuditData();
        log.info("生成已分配未存活内网IP数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 55 5 * * ?")
    @XxlJob("generateSurvivingUnplannedIntranetIpData")
    public ReturnT<String> generateSurvivingUnplannedIntranetIpData(String param) throws Exception{
        log.info("开始生成存活未规划内网IP数据...");
        survivingUnplannedIntranetIpService.generateAuditData();
        log.info("生成存活未规划内网IP数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 35 6 * * ?")
    @XxlJob("generateSurvivingUnassignIntranetIpData")
    public ReturnT<String> generateSurvivingUnassignIntranetIpData(String param) throws Exception{
        log.info("开始生成存活未分配内网IP数据...");
        survivingUnassignIntranetIpService.generateAuditData();
        log.info("生成存活未分配内网IP数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 55 6 * * ?")
    @XxlJob("generateRegistrationExpiredIpData")
    public ReturnT<String> generateRegistrationExpiredIpData(String param) throws Exception{
        log.info("开始生成IP登记已过期数据...");
        registrationExpiredService.genRegistrationExpiredIpData("内网IP");
        registrationExpiredService.genRegistrationExpiredIpData("公网IP");
        registrationExpiredService.genRegistrationExpiredIpData("IPv6");
        log.info("生成IP登记已过期数据结束");
        return ReturnT.SUCCESS;
    }

    //-----------------------公网IP---------------------------
//    @Async
//    @Scheduled(cron = "0 35 7 * * ?")
    @XxlJob("generateSurvivingUnplanPublicIpData")
    public ReturnT<String> generateSurvivingUnplanPublicIpData(String param) throws Exception{
        log.info("开始生成存活未规划公网IP数据...");
        survivingUnplanPublicIpService.generateAuditData();
        log.info("生成存活未规划公网IP数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 2 8 * * ?")
    @XxlJob("generateSurvivingUnassignPublicIpData")
    public ReturnT<String> generateSurvivingUnassignPublicIpData(String param) throws Exception{
        log.info("开始生成存活未分配公网IP数据...");
        survivingUnassignPublicIpService.generateAuditData();
        log.info("生成存活未分配公网IP数据结束");
        return ReturnT.SUCCESS;
    }

    //-----------------------IPv6-----------------------------
//    @Async
//    @Scheduled(cron = "0 20 8 * * ?")
    @XxlJob("generateSurvivingUnassignIpV6Data")
    public ReturnT<String> generateSurvivingUnassignIpV6Data(String param) throws Exception{
        log.info("开始生成存活未分配IPv6数据...");
        survivingUnassignIpV6Service.generateAuditData();
        log.info("生成存活未分配IPv6数据结束");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 30 8 * * ?")
    @XxlJob("generateSurvivingUnplanIpv6Data")
    public ReturnT<String> generateSurvivingUnplanIpv6Data(String param) throws Exception{
        log.info("开始生成存活未规划IPv6数据...");
        survivingUnplanIpv6Service.generateAuditData();
        log.info("生成存活未规划IPv6数据结束");
        return ReturnT.SUCCESS;
    }


}
