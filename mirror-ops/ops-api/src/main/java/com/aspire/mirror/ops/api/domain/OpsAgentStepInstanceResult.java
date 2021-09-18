/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsAgentStepInstanceResult.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsAgentStepInstanceResult
 * <p/>
 *
 * 类功能描述: step对应的每个agent执行结果记录
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class OpsAgentStepInstanceResult {
	@JsonProperty("step_instance_id")
	private Long	stepInstanceId;

	@JsonProperty("target_host")
	private String	targetHost;		// proxyId:hostIp
	
	@JsonProperty("pool")
	private String pool;
	
	@JsonProperty("agent_ip")
	private String	agentIp;
	
	@JsonProperty("ops_type")
	private Integer opsType;

	@JsonProperty("status")
	private Integer	status;

	@JsonProperty("exit_code")
	private Integer	exitCode;

	@JsonProperty("ops_log")
	private String	opsLog;
	
	@JsonProperty("aspnode_result")
	private Integer	aspNodeResult;		// 从脚本执行结果中提取出来的  aspnode_result 标记位         0: 成功     1: 失败
	
	@JsonProperty("aspnode_msg")
	private String	aspNodeMessage;		

	@JsonProperty("mark_time")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	markTime;

	@JsonProperty("total_time")
	protected Float	totalTime;
	
	public Float calculateTotalTime(Date beginTime) {
		if (markTime == null || beginTime == null) {
			return null;
		}
		totalTime = (markTime.getTime() - beginTime.getTime()) / 1000f;
		return totalTime;
	}
	
	public static class OpsAgentStepInstanceResultQueryModel extends OpsAgentStepInstanceResult {
		@JsonProperty("page_size")
		private Integer	pageSize;		// 如果为null， 则查询全部数据
		@JsonProperty("page_no")
		private Integer	pageNo;			// 从0开始

		public Integer getStartIdx() {
			if (pageSize == null) {
				return null;
			}
			return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
		}
	}
}
