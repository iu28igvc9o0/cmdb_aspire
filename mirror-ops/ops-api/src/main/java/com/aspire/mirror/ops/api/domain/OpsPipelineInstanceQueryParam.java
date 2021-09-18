/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsPipelineInstanceQueryParam.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月2日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsPipelineInstanceQueryParam
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月2日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Setter
@Getter
public class OpsPipelineInstanceQueryParam extends AbstractPageListQueryParam {
	private static final String	DEFAULT_ORDER_COLUMN	= "pipeline_instance_id";
	private static final String	DEFAULT_ORDER_TYPE		= "DESC";
	
	@JsonProperty("pipeline_instance_id_list")
	protected List<Long>		pipelineInstanceIdList;
	@JsonProperty("pipeline_instance_name")
	protected String			pipelineInstanceName;
	@JsonProperty("operator")
	protected String			operator;								// 操作发起人
	@JsonProperty("trigger_way")
	protected Integer			triggerWay;
	@JsonProperty("status")
	protected Integer			status;									// 作业当前状态
	@JsonIgnore
	protected Integer			bizClassify;							// 业务分类  
	@JsonProperty("start_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				startTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("end_time")
	protected Date				endTime;

	@JsonProperty("pipeline_instance_id")
	protected Long				pipelineInstanceId;
	
	public OpsPipelineInstanceQueryParam() {
		super.orderColumn = DEFAULT_ORDER_COLUMN;
		super.orderType = DEFAULT_ORDER_TYPE;
	}
	
	@Override
	public void setOrderType(String orderType) {
		if (StringUtils.isNotEmpty(orderType)) {
			super.orderType = orderType;
			return;
		}
	}
//	public static final String[] orderNameArray  = new String [] {"start_time", "end_time", "status",  "trigger_way"};
	@Override
	public void setOrderColumn(String orderColumn) {
		if (StringUtils.isNotEmpty(orderColumn)) {
			super.orderColumn = orderColumn;
		}
	}
}
