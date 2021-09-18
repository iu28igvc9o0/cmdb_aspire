package com.aspire.mirror.alert.server.biz.cabinetAlert;

import java.util.List;

import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;

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
public interface AlertCabinetColumnCommonBiz {
	
	 void delConfigData(AlertCabinetColumnConfig config);
   
	List<AlertCabinetColumnConfigData> getConfigData(AlertCabinetColumnConfig config);


	
}
