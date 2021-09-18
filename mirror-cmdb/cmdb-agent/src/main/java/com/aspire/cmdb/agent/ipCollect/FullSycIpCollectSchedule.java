package com.aspire.cmdb.agent.ipCollect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.automate.service.AutomateService;
import com.aspire.ums.cmdb.bpmx.sendOrder.CmdbAutoStatusMonitorSendOrderTask;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpClashService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpCollectService;
import com.aspire.ums.cmdb.ipCollect.service.ReportAlarmEmailService;
import com.aspire.ums.cmdb.ipCollect.service.VmwareFullSyncApiService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * 存活IP、虚拟IP采集，全量同步
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/3 16:54
 */
@Slf4j
@Component
public class FullSycIpCollectSchedule {

    @Autowired
    private VmwareFullSyncApiService vmwareFullSyncApiService;
    @Autowired
    private CmdbIpCollectService cmdbIpCollectService;
    @Autowired
    private AutomateService automateService;
    @Autowired
    private CmdbIpClashService cmdbIpClashService;
    @Autowired
    private ReportAlarmEmailService reportAlarmEmailService;

    @Autowired
    private CmdbAutoStatusMonitorSendOrderTask autoStatusMonitorSendOrderTask;

    // @Async
    // @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    // public void testScheduled() {
    //     log.info("测试定时任务：" + Thread.currentThread().getName() + " cron=*/2 * * * * ? --- " + new Date());
    // }

//    @Async
//    @Scheduled(cron = "0 0 */3 * * ?")
    @XxlJob("startSyncIpAddressPool")
    public ReturnT<String>  startSyncIpAddressPool(String param) throws Exception {
        log.info("startSyncIpAddressPool");
        vmwareFullSyncApiService.syncIpAddressPool();

        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 40 */3 * * ?")
    @XxlJob("startSyncIpConfPool")
    public ReturnT<String> startSyncIpConfPool(String param)  throws Exception{
        log.info("startSyncIpConfPool");
        vmwareFullSyncApiService.syncIpConfPool();
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 0 */4 * * ?")
    @XxlJob("startSyncIpArpPool")
    public ReturnT<String> startSyncIpArpPool(String param)  throws Exception{
        log.info("startSyncIpArpPool");
        vmwareFullSyncApiService.syncIpArpPool();
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 20 */4 * * ?")
    @XxlJob("startSyncVipAddressPool")
    public ReturnT<String> startSyncVipAddressPool(String param)  throws Exception{
        log.info("startSyncVipAddressPool");
        vmwareFullSyncApiService.syncVipAddressPool();
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 40 */4 * * ?")
    @XxlJob("startSynCmdbBusiness")
    public ReturnT<String> startSynCmdbBusiness(String param) throws Exception{
        log.info("begin_startSynCmdbBusiness>>>>>>>>");
        cmdbIpCollectService.updateIpBusinessByAsset();
        log.info("end_startSynCmdbBusiness>>>>>>>>");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 30 12 * * ? ")
    @XxlJob("startSynSurvivalInfo")
    public ReturnT<String> startSynSurvivalInfo(String param) throws Exception{
        log.info("begin_startSynSurvivalInfo>>>>>>>>");
        // CMDB资产的IP状态信息同步更新到ip内网地址库
        cmdbIpCollectService.updateCmdbAssetAllIpInfo();
        // 更新公网ip信息到地址库
        cmdbIpCollectService.updatePublicIpInfo();
        // 更新ipv6数据到地址库
        cmdbIpCollectService.updateIpv6Info();
        log.info("end_startSynSurvivalInfo>>>>>>>>");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 45 3 * * ? ")
    @XxlJob("startSynSurvivalFirstTimeInfo")
    public ReturnT<String> startSynSurvivalFirstTimeInfo(String param) throws Exception{
        log.info("begin_startSynSurvivalFirstTimeInfo>>>>>>>>");
        // 定期更新首次存活时间
        cmdbIpCollectService.updateFirstSurvivalTime4IpInfo();
        log.info("end_startSynSurvivalFirstTimeInfo>>>>>>>>");
        return ReturnT.SUCCESS;
    }

//    @Async
//    @Scheduled(cron = "0 15 4 * * ? ")
    @XxlJob("startSynIpClashInfo")
    public ReturnT<String> startSynIpClashInfo(String param) throws Exception{
        log.info("begin_startSynIpClashInfo>>>>>>>>");
        // 定期同步自动化二期冲突IP
        cmdbIpCollectService.buildAndSaveIpClashList4Now();
        log.info("end_startSynIpClashInfo>>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSynAutomateConf")
    public ReturnT<String> startSynAutomateConf(String param) throws Exception{
        log.info("begin_startSynAutomateConf >>>>>>>>");
        // 同步主机配置文件
        automateService.synAutomateConfFile();
        log.info("end_startSynAutomateConf >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("autoBuidThatDayPendingIpAndSendOrder")
    public ReturnT<String> autoBuidThatDayPendingIpAndSendOrder(String param) throws Exception{
        log.info("begin_autoBuidThatDayPendingIpAndSendOrder>>>>>>>>");
        //查询当天状态为待处理的IP，发送通用任务流程工单
        cmdbIpClashService.buidThatDayPendingIpAndSendOrder();
        log.info("end_autoBuidThatDayPendingIpAndSendOrder>>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSynInnerIpFreeCount")
    public ReturnT<String> startSynInnerIpFreeCount(String param) throws Exception{
        log.info("begin_startSynInnerIpFreeCount >>>>>>>>");
        // 更新内网IP的可用数量
        cmdbIpCollectService.updateInnerIpFreeCount();
        log.info("end_startSynInnerIpFreeCount >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    /**
     * 更新cmdb资产存活状态为"已存活"
     *
     * @param
     * @return
     */
    @XxlJob("startUpdateCmdbAssetSurvialInfo")
    public ReturnT<String> startUpdateCmdbAssetSurvialInfo(String param) throws Exception{
        log.info("begin_startUpdateCmdbAssetSurvialInfo >>>>>>>>");
        XxlJobLogger.log("开始执行更新cmdb资产为“已存活”.");
        // 更新cmdb资产存活状态
        // cmdbIpCollectService.updateCmdbAssetSurvialInfo();
        cmdbIpCollectService.updateCmdbAssetSurvival();
        log.info("end_startUpdateCmdbAssetSurvialInfo >>>>>>>>");
        XxlJobLogger.log("执行更新cmdb资产为“已存活”完成.");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startFindCmdbAssetSurvial2Kafka")
    public ReturnT<String> startFindCmdbAssetSurvial2Kafka(String param) throws Exception{
        log.info("begin_startFindCmdbAssetSurvial2Kafka >>>>>>>>");
        // 推送资产存活状态到kafka上
        cmdbIpCollectService.findCmdbAssetSurvial2Kafka();
        log.info("end_startFindCmdbAssetSurvial2Kafka >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSynAllNetworkPortGroup")
    public ReturnT<String> startSynAllNetworkPortGroup(String param) throws Exception{
        log.info("begin_startSynAllNetworkPortGroup >>>>>>>>");
        // 全量同步网段-端口组数据
        cmdbIpCollectService.synAllNetworkPortGroup();
        log.info("end_startSynAllNetworkPortGroup >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSendAndGetReportAlarmContent")
    public ReturnT<String> startSendAndGetReportAlarmContent(String param) throws Exception{
        log.info("begin_startSendAndGetReportAlarmContent >>>>>>>>");
        // 存活IP相关报表数据告警处理
        reportAlarmEmailService.sendAndGetReportAlarmContent("report");
        log.info("end_startSendAndGetReportAlarmContent >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    /**
     * 更新cmdb资产存活状态为 "未存活",在 “startUpdateCmdbAssetSurvialInfo” 后执行
     *
     * @param
     * @return
     */
    @XxlJob("updateCmdbDevice2UnSurvival")
    public ReturnT<String> updateCmdbDevice2UnSurvival(String param) throws Exception {
        log.info("begin_updateCmdbDevice2UnSurvival >>>>>>>>");
        XxlJobLogger.log("开始执行更新cmdb资产为“未存活”.");
        // 更新cmdb资产未存活状态
        cmdbIpCollectService.updateCmdbAsset2UnSurvival();
        log.info("end_updateCmdbDevice2UnSurvival >>>>>>>>");
        XxlJobLogger.log("执行更新cmdb资产为“未存活”完成.");
        return ReturnT.SUCCESS;
    }

    @XxlJob("synAllInnerIpSurvival")
    public ReturnT<String> synAllInnerIpSurvival(String param) throws Exception {
        log.info("begin_synAllInnerIpSurvival >>>>>>>>");
        XxlJobLogger.log("开始执行内网地址库的存活状态为“未存活”.");
        // 更新内网地址库的存活状态为未存活
        cmdbIpCollectService.synAllInnerIpSurvival();
        log.info("end_synAllInnerIpSurvival >>>>>>>>");
        XxlJobLogger.log("执行内网地址库的存活状态为“未存活”完成.");
        return ReturnT.SUCCESS;
    }

    /**
     * 自动化采集异常设备 报表任务.
     *
     * @param param
     *            任务参数
     * @return
     */
    @XxlJob("reportCmdbAutoStatus")
    public ReturnT<String> reportCmdbAutoStatus(String param) throws Exception {
        log.info("begin_updateCmdbAutoStatus >>>>>>>>");
        XxlJobLogger.log("开始执行[自动化采集异常设备报表].");
        // 更新cmdb资产存活状态
        // cmdbIpCollectService.updateCmdbAssetSurvialInfo();
        cmdbIpCollectService.reportCmdbAutoStatus();
        log.info("end_reportCmdbAutoStatus >>>>>>>>");
        XxlJobLogger.log("执行[自动化采集异常设备报表]完成.");
        return ReturnT.SUCCESS;
    }

    /**
     * 自动化采集异常设备 派发工单 任务,在“reportCmdbAutoStatus”后执行
     *
     * @param
     * @return
     */
    @XxlJob("autoStatusMonitorSendOrder")
    public ReturnT<String> autoStatusMonitorSendOrder(String param) throws Exception {
        log.info("begin_autoStatusMonitorSendOrderTask >>>>>>>>");
        XxlJobLogger.log("开始执行[自动化采集异常设备]派发工单.");
        autoStatusMonitorSendOrderTask.execute();
        log.info("end_autoStatusMonitorSendOrderTask >>>>>>>>");
        XxlJobLogger.log("执行[自动化采集异常设备]派发工单完成.");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSynAssetOtherIp")
    public ReturnT<String> startSynAssetOtherIp(String param) throws Exception{
        log.info("begin_startSynAssetOtherIp >>>>>>>>");
        // 更新同步资产的其他IP
        cmdbIpCollectService.updateAssetOtherIp();
        log.info("end_startSynAssetOtherIp >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    @XxlJob("startSynUpdateBaseCollectIp")
    public ReturnT<String> startSynUpdateBaseCollectIp(String param) throws Exception{
        log.info("begin_startSynUpdateBaseCollectIp >>>>>>>>");
        // 通过存活IP,更新IP地址库的存活和分配信息
        cmdbIpCollectService.updateBaseCollectIp();
        log.info("end_startSynUpdateBaseCollectIp >>>>>>>>");
        return ReturnT.SUCCESS;
    }

    /**
     * IP管理资产表根据资产表的管理ip、业务ip1、业务ip2、consoleip、other_ip（自动化采集ip）和关联表的“其他ip”，同步资产表的承载网IP字段，若不存在则追加
     * @param param
     * @return
     * @throws Exception
     */
    @XxlJob("initCarrierNetworkForAsset")
    public ReturnT<String> initCarrierNetworkForAsset(String param) throws Exception {
        log.info("begin_initCarrierNetworkForAsset >>>>>>>>");
        XxlJobLogger.log("开始执行[初始化资产表的承载网IP] >>>>>>>>>");
        // 初始化资产表的承载网IP
        cmdbIpCollectService.initCarrierNetworkForAsset();
        log.info("end_initCarrierNetworkForAsset >>>>>>>>");
        XxlJobLogger.log("执行完成[初始化资产表的承载网IP] >>>>>>>>>");
        return ReturnT.SUCCESS;
    }

}
