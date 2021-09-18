package com.aspire.mirror.zabbixintegrate.biz;

import java.util.Date;

public interface HwMonitorSyncService {

	void syncMonitorDatas(Date startTime,Date endTime) throws Exception;

	void syncDeviceData() throws Exception;

	void syncMonitorConfigData() throws Exception;

}
