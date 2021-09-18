package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.basicdatasync.client.BasicDataSyncResponse;
import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;

@Component
class MonitorTriggerDataListSync extends AbstractBasicDataListSync<MonitorTriggerRecord> {
    @Value("${basicDataSync.url}")
	private String syncUrl;
//    @Autowired
//    private IBasicDataSyncClient client;

    @Override
    public String getSyncItemIdentity() {
        return "monitor_triggers";
    }

    @Override
    protected Pair<Integer, List<MonitorTriggerRecord>> fetchsyncItemDataList0() {
		IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
        BasicDataSyncResponse<MonitorTriggerRecord> syncDataResult = client.syncMonitorTriggerList(super.getStartSyncSeq(), super.proxyIdentityConfig.getId());
        return Pair.of(syncDataResult.getLastSyncSeq(), syncDataResult.getItemDataList());
    }

    @Override
    protected void processUpdateBasicData(MonitorTriggerRecord dataItem) {
        super.basicDataBiz.saveMonitorTrigger(dataItem);
    }

    @Override
    protected void processRemoveBasicData(MonitorTriggerRecord dataItem) {
        super.basicDataBiz.removeMonitorTrigger(dataItem.getTriggerId());
    }
}
