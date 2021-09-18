package com.aspire.mirror.elasticsearch.api.dto;


import java.io.Serializable;

import lombok.Data;

@Data
public class DataSet implements Serializable {

	private static final long serialVersionUID = 1L;

	//查询条件 
	private String conditionField;
	private String itemId;
	private String subItemIds;
	private String itemViewName;
	private String reportTpye;
	//统计方式
	private String countType;
	//统计字段:value
	private String countField;
	//间隔时间(选时间间隔)
	private String intervalTime;
	//间隔单位(选时间间隔)  year,month,day,hour,week,minute
	private String intervalUnit;
	//x轴字段(不选时间间隔):clock
	private String xLineColumn;
	//查看时间:1、3、6、12
	private String minTimeValue;
	
	private Long startTime;
	
	private Long endTime;
	//1业务系统2资源池3机房4设备大类5设备小类
	private Integer resourceType;
	
	private String resourceValue;
	
	//查看时间单位:1、3、6、12
	private String minTimeUnit;
	
	private String mointerIndex;
	
	@Override
	public String toString() {
		return "DataSet [itemId=" + itemId + ", itemViewName=" + itemViewName + ", reportTpye="
				+ reportTpye + ", countType=" + countType + ", countField=" + countField
				+ ", intervalTime=" + intervalTime + ", intervalUnit=" +intervalUnit + ", xLineColumn=" + xLineColumn + 
				", resourceType=" + resourceType + ", resourceValue=" + resourceValue +"]";
	}
	

}