package com.aspire.mirror.indexproxy.basicdatasync;

import com.aspire.mirror.indexproxy.basicdatasync.client.BasicDataSyncResponse;
import com.aspire.mirror.indexproxy.basicdatasync.client.IBasicDataSyncClient;
import com.aspire.mirror.indexproxy.client.ClientServiceBuilder;
import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 触发器动态阈值数据同步
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MonitorTriggerDynamicModelDataListSync
 * 类描述:    触发器动态阈值数据同步
 * 创建人:    JinSu
 * 创建时间:  2020/11/11 21:08
 * 版本:      v1.0
 */
@Component
class MonitorTriggerDynamicModelDataListSync extends AbstractBasicDataListSync<MonitorDynamicThresholdRecord> {
    @Value("${basicDataSync.url}")
    private String syncUrl;
//    @Autowired
//    private IBasicDataSyncClient client;

    @Override
    public String getSyncItemIdentity() {
        return "monitor_trigger_model";
    }

    @Override
    protected Pair<Integer, List<MonitorDynamicThresholdRecord>> fetchsyncItemDataList0() {
        IBasicDataSyncClient client = ClientServiceBuilder.buildClientService(IBasicDataSyncClient.class, syncUrl);
        BasicDataSyncResponse<MonitorDynamicThresholdRecord> syncDataResult = client.syncMonitorTriggerDynamicModelList(super.getStartSyncSeq(), super.proxyIdentityConfig.getId());return Pair.of(syncDataResult.getLastSyncSeq(), syncDataResult.getItemDataList());
    }

    @Override
    protected void processUpdateBasicData(MonitorDynamicThresholdRecord dataItem) {
        super.basicDataBiz.saveMonitorTriggerDynamicModel(dataItem);
    }

    @Override
    protected void processRemoveBasicData(MonitorDynamicThresholdRecord dataItem) {
        super.basicDataBiz.removeMonitorTriggerDynamicModel(dataItem.getModelId());
    }
}
