package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportResponse {
	private Integer periodConfigId ;
	private String provinceCode ;
	private String provinceName ;
	private String dictPeriod ;
	private String dictValue ;
	private String fileIndicationName ;
	private Integer fileIndicationId ;
	private String uploadDate ;
	FileStateReportItem data;
	FileStateReportItem preData;
	private Integer fixFileCount;
}
