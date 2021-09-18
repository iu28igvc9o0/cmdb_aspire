package com.aspire.mirror.indexproxy.basicdatasync;

import com.aspire.mirror.indexproxy.helper.DistributeLockHelper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    BasicDataSyncNewJobRunner
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/19 14:57
 * 版本:      v1.0
 */
@Component
@Slf4j
public class BasicDataSyncNewJobRunner {
    @Autowired
    private AbstractMonitorObjectDataListSync monitorObjectDataListSync;

    @Autowired
    private DistributeLockHelper disLockHelper;

    @Autowired
    private List<IBasicDataListSync<?>> dataListSyncList;

    @Value("${proxyIdentityConfig.id}")
    private String proxyIdentity;

    @Value("${basicDataSync.dynamicModelInterval}")
    private Integer						dynamicModelInterval;

    @XxlJob("MonitorBasicDataSyncJobHandler")
    public ReturnT<String> basicDataSync(String param) throws Exception {
        // 获取分布式锁
        String syncLockName = proxyIdentity + "_sync";

        final Lock syncLock = disLockHelper.getReadWriteLock(syncLockName).writeLock();
        try {
            boolean flagLock = syncLock.tryLock(5, TimeUnit.SECONDS);
            if (!flagLock) {
                log.warn("MonitorBasicDataSyncJobHandler get lock is error!");
                return ReturnT.FAIL;
            }
            // 监控对象
            XxlJobLogger.log("XXL-JOB, start sync monitor object data.{}", System.currentTimeMillis());
            monitorObjectDataListSync.getMonitorObject();
            monitorObjectDataListSync.processSyncDataList();
            // 基础数据
            XxlJobLogger.log("XXL-JOB, start sync basic data.{}", System.currentTimeMillis());
            for (IBasicDataListSync<?> syncItem : dataListSyncList) {
                syncItem.fetchSyncItemDataList();
                syncItem.processSyncDataList();
            }
            XxlJobLogger.log("XXL-JOB, end sync template basic data.{}", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error to sync datalist.", e);
        } finally {
            syncLock.unlock();
        }
        return ReturnT.SUCCESS;
    }
}
