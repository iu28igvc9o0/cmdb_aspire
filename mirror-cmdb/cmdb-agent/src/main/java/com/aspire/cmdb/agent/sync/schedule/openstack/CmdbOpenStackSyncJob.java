package com.aspire.cmdb.agent.sync.schedule.openstack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.openstack.service.OpenstackFullImageSyncService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * openStack全量同步接口
 *
 * @author jiangxuwen
 * @date 2020/12/2 16:47
 */
@Component
@Slf4j
public class CmdbOpenStackSyncJob {

    @Autowired
    private OpenstackFullImageSyncService openstackFullImageSyncService;

    @XxlJob("openStackSubnetSyncJob")
    public ReturnT<String> openStackSubnetSyncJob(String param) throws Exception {
        log.info("开始执行[openStackSubnet]任务");
        XxlJobLogger.log("开始执行[openStackSubnet]任务");
        try {
            openstackFullImageSyncService.syncOpenstackSubnet();
        } catch (Exception e) {
            log.info("执行[openStackSubnet]任务失败.", e);
            XxlJobLogger.log("执行[openStackSubnet]任务失败.", e);
            return ReturnT.FAIL;
        }
        log.info("执行[openStackSubnet]任务完成");
        XxlJobLogger.log("执行[openStackSubnet]任务完成");
        return ReturnT.SUCCESS;
    }

    @XxlJob("openStackImageSyncJob")
    public ReturnT<String> openStackImageSyncJob(String param) throws Exception {
        log.info("开始执行[openStackImage]任务");
        XxlJobLogger.log("开始执行[openStackImage]任务");
        try {
            openstackFullImageSyncService.syncOpenstackImage();
        } catch (Exception e) {
            log.info("执行[openStackImage]任务失败.", e);
            XxlJobLogger.log("执行[openStackImage]任务失败.", e);
            return ReturnT.FAIL;
        }
        log.info("执行[openStackImage]任务完成");
        XxlJobLogger.log("执行[openStackImage]任务完成");
        return ReturnT.SUCCESS;
    }
}
