package com.aspire.mirror.inspection.server.biz;

import com.aspire.mirror.inspection.api.dto.ReportItemCallBackRequest;

/**
* 巡检指标回调处理    <br/>
* Project Name:inspection-service
* File Name:ReportItemCallBackBiz.java
* Package Name:com.aspire.mirror.inspection.server.biz
* ClassName: ReportItemCallBackBiz <br/>
* date: 2018年8月23日 下午4:05:04 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface ReportItemCallBackBiz {
	/**
	* 处理巡检报表监控项回调. <br/>
	*
	* 作者： pengguihua
	* @param bizObj
	*/  
	public void onCallBack(ReportItemCallBackRequest bizObj);
}
