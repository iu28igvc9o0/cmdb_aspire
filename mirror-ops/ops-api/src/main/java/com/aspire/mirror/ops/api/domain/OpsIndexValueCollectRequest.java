/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsIndexValueCollectModel.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月14日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsIndexValueCollectRequest
 * <p/>
 *
 * 类功能描述: 指标采集请求对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月14日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OpsIndexValueCollectRequest {
	@JsonProperty("script_id")
	private long								scriptId;

	@JsonProperty("script_param")
	private String scriptParam;

	@JsonProperty("customize_param")
	private String customizeParam;

	@JsonIgnore
	private OpsScript							scriptData;

	@JsonProperty("timeout")
	private Integer								timeout;

	@JsonProperty("callback_type")
	private String								callbackType;

	@JsonProperty("callback_target")
	private String								callbackTarget;

	@JsonProperty("target_ops_user")
	private String								targetOpsUser;
	
	@JsonProperty("target_agent_list")
	private List<IndexValueCollectAgentInfo>	collectTargetList;
	
	
	public OpsMessageExtendMeta constructAgentResultMeta() {
		OpsMessageExtendMeta extendMeta = new OpsMessageExtendMeta(OpsPipelineInstanceDTO.BIZ_CLASSIFY_INDEX_COLLECT);
		extendMeta.addExtendAttr("callbackType", callbackType);
		extendMeta.addExtendAttr("callbackTarget", callbackTarget);
		collectTargetList.parallelStream().forEach(agent -> {
			extendMeta.addExtendAttr(agent.getAgentHost(), agent.getResultMessage());
		});
		return extendMeta;
	}

	@Data
	@JsonInclude(Include.NON_NULL)
	public static class IndexValueCollectAgentInfo {
		@JsonProperty("agent_host")
		private String				agentHost;		// proxyId:IP

		@JsonProperty("result_message")
		private Map<String, Object>	resultMessage;
	}
}
