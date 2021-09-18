package com.aspire.mirror.indexproxy.basicdatasync;

import com.aspire.mirror.indexproxy.basicdatasync.client.BasicDataSyncResponse;
import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MonitorActionDataListSync extends AbstractBasicDataListSync<MonitorActionRecord> {
	@Value("${basicDataSync.url}")
	private String syncUrl;
//
//	@Autowired
//	private IBasicDataSyncClient client;

	@Override
	public String getSyncItemIdentity() {
		return "monitor_actions";
	}
	
	@Override
	protected Pair<Integer, List<MonitorActionRecord>> fetchsyncItemDataList0() {
		IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
		BasicDataSyncResponse<MonitorActionRecord> syncDataResult = client.syncMonitorActionList(super.getStartSyncSeq(), super.proxyIdentityConfig.getId());
		return Pair.of(syncDataResult.getLastSyncSeq(), syncDataResult.getItemDataList());
	}
	
	@Override
	protected void processUpdateBasicData(MonitorActionRecord dataItem) {
		super.basicDataBiz.saveMonitorAction(dataItem);
	}
	
	@Override
	protected void processRemoveBasicData(MonitorActionRecord dataObj) {
		super.basicDataBiz.removeMonitorAction(dataObj.getActionId());
	}
}
