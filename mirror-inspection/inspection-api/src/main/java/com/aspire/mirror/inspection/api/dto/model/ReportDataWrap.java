package com.aspire.mirror.inspection.api.dto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 报表数据封装类    <br/>
* Project Name:inspection-service
* File Name:ReportDataWrap.java
* Package Name:com.aspire.mirror.inspection.server.biz.domain
* ClassName: ReportDataWrap <br/>
* date: 2018年8月27日 下午3:26:29 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_EMPTY)
public class ReportDataWrap {
	@JsonProperty("report_id")
	private String reportId;
	// 报表名
	@JsonProperty
	private String name;

	@JsonProperty("task_name")
	private String taskName;

    @JsonProperty("finish_time")
    private Date finishTime;

	@JsonProperty("device_num")
	private Integer deviceNum;

	@JsonProperty("task_type")
	private String taskType;

	@JsonProperty("create_time")
	private Date createTime;

	@JsonProperty("item_num")
	private Integer itemNum;
	@JsonProperty("task_id")
	private String taskId;

	@JsonProperty("report_item_list")
    private List<ReportItemDataWrap> reportItemDataList;
    
    @JsonProperty("normal")
    public List<ReportItemDataWrap> getNormalList() {
    	List<ReportItemDataWrap> resultList = new ArrayList<ReportItemDataWrap>();
    	if (reportItemDataList == null) {
    		return resultList;
    	}
    	for (ReportItemDataWrap item : reportItemDataList) {
    		if (ReportItemDTO.STATUS_NORMAL.equals(item.getStatus())) {
    			resultList.add(item);
    		}
    	}
    	return resultList;
    }
    
    @JsonProperty("exception")
    public List<ReportItemDataWrap> getExceptionList() {
    	List<ReportItemDataWrap> resultList = new ArrayList<ReportItemDataWrap>();
    	if (reportItemDataList == null) {
    		return resultList;
    	}
    	for (ReportItemDataWrap item : reportItemDataList) {
    		if (ReportItemDTO.STATUS_EXCEPTION.equals(item.getStatus())) {
    			resultList.add(item);
    		}
    	}
    	return resultList;
    }
    
    @JsonProperty("noResult")
    public List<ReportItemDataWrap> getNoResultList() {
    	List<ReportItemDataWrap> resultList = new ArrayList<ReportItemDataWrap>();
    	if (reportItemDataList == null) {
    		return resultList;
    	}
    	for (ReportItemDataWrap item : reportItemDataList) {
    		if (ReportItemDTO.STATUS_NORESULT.equals(item.getStatus())) {
    			resultList.add(item);
    		}
    	}
    	return resultList;
    }

    @JsonProperty("artificialJudgment")
	public List<ReportItemDataWrap> getArtificialJudgmentList() {
		List<ReportItemDataWrap> resultList = new ArrayList<ReportItemDataWrap>();
		if (reportItemDataList == null) {
			return resultList;
		}
		for (ReportItemDataWrap item : reportItemDataList) {
			if (ReportItemDTO.STATUS_ARTIFICIAL_JUDGMENT.equals(item.getStatus())) {
				resultList.add(item);
			}
		}
		return resultList;
	}
}
