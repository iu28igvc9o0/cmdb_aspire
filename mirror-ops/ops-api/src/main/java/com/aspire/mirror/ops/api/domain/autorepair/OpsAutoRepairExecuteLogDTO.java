/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsAutoRepairOperateLog.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain.autorepair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.api.domain.AbsPageQueryParamsAware;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsAutoRepairExecuteResult
 * <p/>
 *
 * 类功能描述: 自动修复作业执行结果记录
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class OpsAutoRepairExecuteLogDTO {
	protected Long								id;
	protected Long								schemeId;
	protected String							schemeName;
	protected Integer							currentPipelineOrder;
	protected Long								currentPipelineInstanceId;
	protected String							currentPipelineInstanceName;
	protected final List<Long>					executedPipeInstIdList 		= new ArrayList<>();
	protected final List<String>				executedPipeInstNameList 	= new ArrayList<>();
	protected String							sourceMark;
	protected String							apItemType;
	protected String							apItemTypeName;
	protected String							apItem;
	protected String							apItemName;
	protected String							assetPool;
	protected String							agentIp;
	protected final List<Map<String, Object>>	extendDataList	= new ArrayList<>();
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date								triggerTime;
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date								endTime;
	
	protected Float								totalTime;
	protected Integer							status;
	protected Integer							exitCode;
	protected String							response;
	protected Integer							aspNodeResult;		// 从脚本执行结果中提取出来的  aspnode_result 标记位         0: 正常     1: 异常
	protected String							aspNodeMessage;
	
	public void setEndTime(Date	endTime) {
		this.endTime = endTime;
		if (triggerTime == null) {
			return;
		}
		this.totalTime = ((float) (this.endTime.getTime() - this.triggerTime.getTime())) / 1000f;
	}

	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class OpsApExecHistoryQueryModel extends AbsPageQueryParamsAware {
		
		@JsonProperty("scheme_name_like")
		private String	schemeNameLike;
		
		@JsonProperty("pipe_inst_name_like")
		private String pipeInstNameLike;

		@JsonProperty("status")
		private Integer	status;

		@JsonProperty("create_time_start")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private String	createTimeStart;

		@JsonProperty("create_time_end")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private String	createTimeEnd;
	}
}
