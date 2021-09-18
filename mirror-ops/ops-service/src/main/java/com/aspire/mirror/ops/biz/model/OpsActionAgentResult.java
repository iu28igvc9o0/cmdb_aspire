package com.aspire.mirror.ops.biz.model;

import java.util.Date;

import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: OpsActionAgentResult
 * <p/>
 *
 * 类功能描述: agent主机step执行结果
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月24日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public final class OpsActionAgentResult {
	@JsonProperty("meta")
	private OpsMessageMeta			meta;

	@JsonProperty("agent_ops_result")
	private AgentOpsResultDetail	opsResultDetail;
	
	@Data
	public static class AgentOpsResultDetail {
		@JsonProperty("proxy_id")
		private Long	proxyId;

		@JsonProperty("ip_address")
		private String	ipAddress;

		@JsonProperty("success")
		private boolean	success;

		@JsonProperty("status_flag")
		private Integer	statusFlag;

		@JsonProperty("exit_code")
		private Integer	exitCode;

		@JsonProperty("ops_log")
		private String	opsLog;
		
		@JsonProperty("aspnode_result")
		private Integer	aspNodeResult;			// 从脚本执行结果中提取出来的  aspnode_result 标记位         0: 成功     1: 失败
		
		@JsonProperty("aspnode_msg")
		private String	aspNodeMessage;			// 从脚本执行结果中提取出来的  aspnode_msg 内容
		
		@JsonProperty("is_over")
		private boolean	over;					// 反馈是否完成
		
		public String getConcatHost() {
			return proxyId + ":" + ipAddress;
		}
		
		/** 
		 * aspnode_result标记位 枚举
		 */
		@Getter
		public static enum AspNodeResultFlagEnum {
			STATUS_SUC(0, "执行成功"), STATUS_FAIL(1, "执行失败");

			private AspNodeResultFlagEnum(Integer code, String label) {
				this.code = code;
				this.label = label;
			}

			private Integer	code;
			private String	label;
		}
	}
	
	public OpsAgentStepInstanceResult retrieveAgentStepInstanceResult() {
		OpsAgentStepInstanceResult result = new OpsAgentStepInstanceResult();
		result.setStepInstanceId(meta.getStepInstanceId());
		result.setTargetHost(opsResultDetail.getConcatHost());
		result.setStatus(opsResultDetail.getStatusFlag());
		result.setExitCode(opsResultDetail.getExitCode());
		result.setAspNodeResult(opsResultDetail.getAspNodeResult());
		result.setAspNodeMessage(opsResultDetail.getAspNodeMessage());
		result.setOpsLog(opsResultDetail.getOpsLog());
		result.setMarkTime(new Date());
		return result;
	}
}
