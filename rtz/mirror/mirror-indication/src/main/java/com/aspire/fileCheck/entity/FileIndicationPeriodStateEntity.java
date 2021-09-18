package com.aspire.fileCheck.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileIndicationPeriodStateEntity {
	private Integer periodStateId ;
	private Integer periodConfigId ;
	private Integer fileIndicationId;
	private String minutePeriod;
	private Integer uploadStatus;
	private double updateIntegrity ;
	private Integer fileSizeStatus;
	private String uploadDate ;
	private Date createTime ;
	private Date modifyTime ;
}
