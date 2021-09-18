package com.aspire.mirror.indexproxy.basicdatasync;

import com.aspire.mirror.indexproxy.helper.DistributeLockHelper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    BasicMonitorObjectRunner
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/11 20:35
 * 版本:      v1.0
 */
@Component
public class BasicMonitorObjectRunner {
    @Autowired
    private AbstractMonitorObjectDataListSync monitorObjectDataListSync;

    @Autowired
    private DistributeLockHelper disLockHelper;

    @Autowired
    private List<IBasicDataListSync<?>> dataListSyncList;

    @Value("${proxyIdentityConfig.id}")
    private String proxyIdentity;

    @Value("${basicDataSync.interval}")
    private Integer interval;

    @XxlJob("MonitorObjectJobHandler")
    public ReturnT<String> templateSync(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, start sync monitor object data.");
        monitorObjectDataListSync.getMonitorObject();
        monitorObjectDataListSync.processSyncDataList();


//        if (StringUtils.isEmpty(param)) {
//            monitorObjectDataListSync.getMonitorObject();
//            monitorObjectDataListSync.processSyncDataList();
//        } else {
//            // TODO 增量同步
//            monitorObjectDataListSync.getMonitorObject();
//            monitorObjectDataListSync.processSyncDataList();
//        }
        return ReturnT.SUCCESS;
    }
}
