package com.aspire.mirror.inspection.server.biz;

import com.aspire.mirror.inspection.api.dto.model.InspectionCountQueryModel;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountResp;
import com.aspire.mirror.inspection.api.dto.model.OpsTimeConsumeStatisticBase;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;

import java.util.Map;

/**
* 报表数据生成服务    <br/>
* Project Name:inspection-service
* File Name:ReportDataGenerateBiz.java
* Package Name:com.aspire.mirror.inspection.server.biz
* ClassName: ReportDataGenerateBiz <br/>
* date: 2018年8月24日 下午2:23:33 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface ReportDataGenerateBiz {
	
	ReportDataWrap generateReportData(String reportId);
	
	void reportDataNotify(ReportDataWrap reportData);

    ReportDataWrap getBaseInfo(String reportId);

    InspectionCountResp getInspectionCount(InspectionCountQueryModel inspectionCountQueryModel);

    OpsTimeConsumeStatisticBase getInspectionTimeStatistic(Map<String,Object> param);
}
