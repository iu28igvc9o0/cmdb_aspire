package com.aspire.mirror.indexproxy.basicdatasync;

import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.domain.MirrorHostVO;
import com.aspire.mirror.indexproxy.domain.MonitorHost;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MonitorHostDataSync
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/11 21:08
 * 版本:      v1.0
 */
@Component
public class MonitorHostDataSync extends AbstractMonitorObjectDataListSync<MirrorHostVO> {
    //    @Autowired
//    private IBasicDataSyncClient iBasicDataSyncClient;
    @Value("${basicDataSync.url}")
    private String syncUrl;

    @Autowired
    private ProxyIdentityConfig proxyIdentityConfig;

    @Override
    protected List<MirrorHostVO> fetchsyncItemDataList0() {
        IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
        return client.getMonitorHostByProxyIdentity(proxyIdentityConfig.getId());
    }

    @Override
    protected String getMonitorType() {
        // 自监控设备类型
        return MonitorObjectRecord.Object_TYPE_SELF_MONITOR;
    }

    @Override
    protected List<MonitorObjectRecord> processUpdateMonitorData(List<MirrorHostVO> hostList) {
        List<MonitorObjectRecord> monitorObjectRecords = Lists.newArrayList();
        for (MirrorHostVO monitorHost : hostList) {
            MonitorObjectRecord monitorObjectRecord = monitorHost.parseToMonitorObject();
            monitorObjectRecords.add(monitorObjectRecord);
        }
        return monitorObjectRecords;
    }

}
