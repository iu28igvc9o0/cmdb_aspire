package com.aspire.mirror.alert.server.biz.cabinetAlert;

import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;
import com.aspire.mirror.alert.server.vo.cabinetAlert.AlertCabinetColumnVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

/**
 * 告警业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.biz.impl
 * 类名称:    AlertsHisBiz.java
 * 类描述:    告警业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface AlertCabinetColumnBiz {
   

	void updateConfig(List<AlertCabinetColumnConfig> configList);

	AlertCabinetColumnConfig getConfig(AlertCabinetColumnVo req);

	void updateConfigData(AlertCabinetColumnVo req);

	 PageResponse<AlertCabinetColumnConfigData> queryCabinetColumnAlertPageList(PageRequest pageRequest);

	List<AlertCabinetColumnConfigData> queryCabinetColumnAlert(PageRequest pageRequest);

	PageResponse<Map<String, Object>> queryCabinetAlertPageList(PageRequest pageRequest);

	List<Map<String,Object>> queryCabinetAlert(PageRequest pageRequest);

	String getScheduleConfig(String indexType);
	
	PageResponse<Map<String, Object>> queryDeviceAlertPageList(PageRequest pageRequest);

	PageResponse<Map<String, Object>> queryBizSystemAlertPageList(PageRequest page);

	List<Map<String, Object>> queryBizSystemAlertList(PageRequest pageRequest);

	boolean getInitialConfigData(AlertCabinetColumnConfig config);

	PageResponse<Map<String, Object>> queryRelateBizsystemList(PageRequest page);

	/**
	 * 列头柜任务
	 * @throws Exception
	 */
	void CabinetColumnTask() throws Exception;
}
