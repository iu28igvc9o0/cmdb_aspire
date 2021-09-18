package com.aspire.fileCheck.entity;

import com.aspire.mirror.entity.IndicationProvinceEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationPeriodConfigEntity {
	private Integer configId ;
	private String provinceCode;
	private Integer indicationId ;
	private Integer dictPeriod ;
	private Integer fixFileCount ;
	private FileIndicationEntity fileIndication;
	private MirrorDictEntity mirrorDict;
	private IndicationProvinceEntity provinceEntity;
	private Integer maxAlarmLimit ;
	private Integer minAlarmLimit ;
	private Integer fixFileSize;
	
}
