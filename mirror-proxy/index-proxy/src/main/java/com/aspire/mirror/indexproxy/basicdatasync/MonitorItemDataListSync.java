package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.basicdatasync.client.BasicDataSyncResponse;
import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;

@Component
class MonitorItemDataListSync extends AbstractBasicDataListSync<MonitorItemRecord> {
    @Value("${basicDataSync.url}")
	private String syncUrl;
//    @Autowired
//    private IBasicDataSyncClient client;

    @Override
    public String getSyncItemIdentity() {
        return "monitor_items";
    }

    @Override
    protected Pair<Integer, List<MonitorItemRecord>> fetchsyncItemDataList0() {
		IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
        BasicDataSyncResponse<MonitorItemRecord> syncDataResult = client.syncMonitorItemList(super.getStartSyncSeq(), super.proxyIdentityConfig.getId());
        return Pair.of(syncDataResult.getLastSyncSeq(), syncDataResult.getItemDataList());
    }

    @Override
    protected void processUpdateBasicData(MonitorItemRecord dataObj) {
        super.basicDataBiz.saveMonitorItem(dataObj);
    }

    @Override
    protected void processRemoveBasicData(MonitorItemRecord dataObj) {
        super.basicDataBiz.removeMonitorItem(dataObj.getItemId());
    }
}
