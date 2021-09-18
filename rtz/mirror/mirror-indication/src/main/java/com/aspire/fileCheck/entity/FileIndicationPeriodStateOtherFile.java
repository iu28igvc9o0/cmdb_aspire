package com.aspire.fileCheck.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationPeriodStateOtherFile {
	private Integer otherFileId ;
	private Integer periodConfigId ;
	private String uploadDate ;
	private Double otherDayCount;
	private Date createTime ;
	private Date modifyTime ;
}
