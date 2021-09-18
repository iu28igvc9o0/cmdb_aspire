package com.aspire.mirror.inspection.server.controller;

import com.aspire.mirror.inspection.api.dto.model.InspectionCountQueryModel;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountResp;
import com.aspire.mirror.inspection.api.dto.model.OpsTimeConsumeStatisticBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.inspection.api.dto.ReportItemCallBackRequest;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;
import com.aspire.mirror.inspection.api.service.ReportDataService;
import com.aspire.mirror.inspection.server.biz.ReportDataGenerateBiz;
import com.aspire.mirror.inspection.server.biz.ReportItemCallBackBiz;

import java.util.Map;

/**
* 巡检报表api接口实现    <br/>
* Project Name:inspection-service
* File Name:ReportDataController.java
* Package Name:com.aspire.mirror.inspection.server.controller
* ClassName: ReportDataController <br/>
* date: 2018年9月3日 下午3:55:59 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@RestController
public class ReportDataController implements ReportDataService {
	@Autowired
	private ReportItemCallBackBiz callbackBiz;
	@Autowired
	private ReportDataGenerateBiz reportDataBiz;
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public ReportDataWrap retriveReportData(@PathVariable("reportId") final String reportId) {
		return reportDataBiz.generateReportData(reportId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void onReportItemDataCallBack(@RequestBody ReportItemCallBackRequest bizObj) {
		callbackBiz.onCallBack(bizObj);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public ReportDataWrap getBaseInfo(@PathVariable("reportId") String reportId) {
		return reportDataBiz.getBaseInfo(reportId);
	}

	@Override
	public InspectionCountResp getInspectionCount(@RequestBody InspectionCountQueryModel inspectionCountQueryModel) {
		return reportDataBiz.getInspectionCount(inspectionCountQueryModel);
	}

	@Override
	public OpsTimeConsumeStatisticBase getInspectionTimeStatistic(@RequestBody Map<String, Object> param) {
		return reportDataBiz.getInspectionTimeStatistic(param);
	}
}
