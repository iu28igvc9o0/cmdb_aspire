package com.aspire.mirror.alert.server.biz.monitorHttp;

import java.util.List;

import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpConfig;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpHis;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpReq;
import com.aspire.mirror.common.entity.PageResponse;

public interface MonitorHttpManageBiz {


	PageResponse<MonitorHttpConfig> pageList(MonitorHttpReq pageRequset);

	MonitorHttpConfig selectByPrimaryKey(String id);

	MonitorHttpConfig insert(MonitorHttpConfig res);

	void update(MonitorHttpConfig res);

	void delete(String id);

	MonitorHttpConfig getByName(String name);

	void updateStatus(MonitorHttpConfig res);

	PageResponse<MonitorHttpHis> pageListHis(MonitorHttpReq pageRequset);

	void batchInsertHis(List<MonitorHttpHis> hisList);

	MonitorHttpHis selectHisByPrimaryKey(String id);

}
