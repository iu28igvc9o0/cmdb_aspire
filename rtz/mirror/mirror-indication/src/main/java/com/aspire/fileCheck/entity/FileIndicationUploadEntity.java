package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationUploadEntity {
	private String uploadDate;
	private Integer periodConfigId;
	private String fileName;
	private String fileUploadTime;
	private String fileCreateTime;
	private Integer dictLazyStatus;
}
