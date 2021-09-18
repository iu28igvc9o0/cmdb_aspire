package com.aspire.mirror.indexproxy.zabbixdatasync;

import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.domain.MonitorHost;
import com.aspire.mirror.indexproxy.domain.MonitorItemDetailResponse;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;

import java.util.List;
import java.util.Map;

public interface TemplateDataListPublisher {
	void publishStandardIndexDataList(List<MonitorTemplateRecord> indexDataList, String proxyIdentity);

    void publishHostDataList(List<MonitorHost> hostList, String proxyIdentity);

    void publishItemData(List<MonitorItemDetailResponse> itemList, String proxyIdentity);
}
