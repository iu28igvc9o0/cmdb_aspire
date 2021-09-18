package com.aspire.fileCheck.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileStateOtherReportEntity {
	private Integer periodConfigId ;
	private String provinceCode ;
	private String dictPeriod ;
	private String provinceName ;
	private String fileIndicationName ;
	private Integer fileIndicationId ;
	private String uploadDate ;
	private Double otherDayCount;
	private Date createTime;
	
	private Double todayDayCount;
	private Double preOtherDayCount;
}
