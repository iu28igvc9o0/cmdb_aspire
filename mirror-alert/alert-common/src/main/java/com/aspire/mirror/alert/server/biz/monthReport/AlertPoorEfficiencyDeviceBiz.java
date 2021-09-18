package com.aspire.mirror.alert.server.biz.monthReport;

import java.util.Date;

public interface AlertPoorEfficiencyDeviceBiz {
	//资源池的设备性能分布(按天)
	void getPoorEfficiencyDeviceMonthData(String item,int count) throws Exception;

	void getPoorEfficiencyDeviceWeekData(String item,int count) throws Exception;
	
	 void queryWeekData(Date curDate,String item) throws Exception ;
	 
	 void queryMonthData(Date curDate,String item) throws Exception ;
	
}
