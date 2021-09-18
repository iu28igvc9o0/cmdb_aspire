package com.aspire.fileCheck.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.aspire.fileCheck.entity.FileIndicationPeriodStateEntity;
import com.aspire.fileCheck.entity.FileStateOtherReportEntity;
import com.aspire.fileCheck.entity.ReportResponse;

public interface IFileIndicationPeriodStateService {

	List<FileIndicationPeriodStateEntity> getDataByDate(String uploadDate);

	int processDate(List<FileIndicationPeriodStateEntity> newItem, List<FileIndicationPeriodStateEntity> updateItem);

	void check(Calendar calendar);

	void checkOtherFile() ;

	Map<String, Map<String, Map<String, ReportResponse>>> getReport(String catolog, String provinceCode, String period,
			String thisDate, String fileIndicationId);

	Map<String,Map<String, FileStateOtherReportEntity>> getOtherFileReport(String catolog, String provinceCode, String period,
																		   String thisDate, String fileIndication);

//	void checkAndEmail(String catalog);

	void setFileCount(String startDate, String endDate);

	void checkFullEmail(String catalog, String checkDate, boolean showOtherFileStats);
}
