package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileStateReportItem {
	private Integer uploadStatus;
	private double updateIntegrity ;
	private String uploadDate ;
	private Integer dictLazyStatus ;
	private Double fileOneHour =0.0;
	private Double fileTowHour =0.0;
	private Double fileTwoMoreHour =0.0;
	private Integer fileDefectCount;
}
