package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationEntity {
	private Integer fileIndicationId ;
	private String fileIndicationCode;
	private String fileIndicationName ;
	private String fileIndicationDir ;
	private String fileIndicationType;
	private List<FileIndicationPeriodConfigEntity> periodConfigList;
}
