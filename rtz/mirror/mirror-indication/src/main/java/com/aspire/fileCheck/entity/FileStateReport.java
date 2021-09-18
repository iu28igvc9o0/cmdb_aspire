package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileStateReport {
	private Integer periodConfigId ;
	private Integer uploadStatus;
	private double updateIntegrity ;
	private String uploadDate ;
	private String provinceCode ;
	private Integer dictLazyStatus ;
	private String dictPeriod ;
	private String dictValue ;
	private String provinceName ;
	private String fileIndicationName ;
	private Integer fileIndicationId ;
	private Integer fixFileCount;
}
