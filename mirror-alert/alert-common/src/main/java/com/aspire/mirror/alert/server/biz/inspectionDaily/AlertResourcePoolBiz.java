package com.aspire.mirror.alert.server.biz.inspectionDaily;

import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertDeviceTypeTop;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertInspectionDaily;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertRourcePoolVo;
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
public interface AlertResourcePoolBiz {
    
    PageResponse<AlertDeviceTypeTop> getDeviceTop10Alert(PageRequest page);
    
    PageResponse<AlertInspectionDaily> getInspectionDaily(PageRequest page);

    List<Map<String,Object>> queryExportData(PageRequest pageRequest);


	void syncAlertTotal(AlertRourcePoolVo pageRequest);

	void syncDeviceTop10Alert(AlertRourcePoolVo pageRequest);

	void syncMoniterTop10Alert(AlertRourcePoolVo pageRequest);

	void syncDistributionAlert(AlertRourcePoolVo pageRequest);

	Map<String, String> getDictMap();

	String[] getDefaultIdcType();

	void deleteCountByMonth(String month);

	void deleteRecordByMonth(String month);

	void deleteDeviceByMonth(String month);

	void deleteMoniterByMonth(String month);
    
}
