package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.basicdatasync.client.BasicDataSyncResponse;
import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;

/**
 * 模版数据同步
 * Project Name:index-proxy
 * File Name:TemplateDataListSync.java
 * Package Name:com.aspire.mirror.indexproxy.basicdatasync
 * ClassName: TemplateDataListSync <br/>
 * date: 2018年8月14日 下午6:09:11 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Component
class MonitorTemplateDataListSync extends AbstractBasicDataListSync<MonitorTemplateRecord> {
    @Value("${basicDataSync.url}")
    private String syncUrl;

//    @Autowired
//    private IBasicDataSyncClient client;

    @Override
    public String getSyncItemIdentity() {
        return "monitor_template";
    }

    @Override
    protected Pair<Integer, List<MonitorTemplateRecord>> fetchsyncItemDataList0() {
        IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
        BasicDataSyncResponse<MonitorTemplateRecord> syncDataResult = client.syncMonitorTemplateList(super.getStartSyncSeq(), super.proxyIdentityConfig.getId());
        return Pair.of(syncDataResult.getLastSyncSeq(), syncDataResult.getItemDataList());
    }

    @Override
    protected void processUpdateBasicData(MonitorTemplateRecord dataItem) {
        super.basicDataBiz.saveMonitorTemplate(dataItem);
    }

    @Override
    protected void processRemoveBasicData(MonitorTemplateRecord dataItem) {
        super.basicDataBiz.removeMonitorTemplate(dataItem.getTemplateId());
    }
}
