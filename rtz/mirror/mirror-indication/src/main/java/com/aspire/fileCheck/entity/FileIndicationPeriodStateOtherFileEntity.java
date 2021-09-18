package com.aspire.fileCheck.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationPeriodStateOtherFileEntity {
	private Integer periodConfigId ;
	private String uploadDate ;
	private Double otherDayCount;
	private Integer count;
	private Date createTime ;
	private Date modifyTime ;
}
