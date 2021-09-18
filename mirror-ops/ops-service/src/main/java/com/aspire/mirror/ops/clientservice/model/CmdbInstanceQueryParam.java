/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  CmdbInstanceQueryParam.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.clientservice.model;

import java.util.HashMap;
import java.util.Map;

import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: CmdbInstanceQueryParam
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class CmdbInstanceQueryParam {
	@JsonProperty
	private final String				token			= "5245ed1b-6345-11e";			// 值固定

	@JsonProperty
	private final String				condicationCode	= "automatic_agent_search";		// 值固定

	@JsonProperty
	private Integer						currentPage		= 1;

	@JsonProperty
	private Integer						pageSize;

//	@JsonProperty
//	private Integer						idcType;			// 移入  queryParams
//
//	@JsonProperty
//	private Integer						ip;					// 移入  queryParams

//	@JsonProperty("is_install_agent")
//	private Integer						isInstallAgent;		// 移入  queryParams

	@JsonIgnore
	protected final Map<String, Object>	queryParams	= new HashMap<String, Object>();	// 查询参数

	@JsonAnyGetter
	public Map<String, Object> getQueryParams() {
		return queryParams;
	}
	
	@JsonAnySetter
	public void addQueryParam(String key, Object value) {
		this.queryParams.put(key, value);
	}
	
	
	public static CmdbInstanceQueryParam from(SimpleAgentHostQueryModel agentParam) {
		CmdbInstanceQueryParam cmdbParam = new CmdbInstanceQueryParam();
		cmdbParam.setCurrentPage(agentParam.getPageNo());
		cmdbParam.setPageSize(agentParam.getPageSize());
		Map<String, Object> extendParams = agentParam.getExtendParams();
		if (extendParams != null) {
			cmdbParam.getQueryParams().putAll(extendParams);
		}
		return cmdbParam;
	}
	
	
	public static CmdbInstanceQueryParam from(String pool, String deviceIp) {
		CmdbInstanceQueryParam cmdbParam = new CmdbInstanceQueryParam();
		cmdbParam.setCurrentPage(1);
		cmdbParam.setPageSize(1);
		cmdbParam.addQueryParam("idcType", pool);
		cmdbParam.addQueryParam("ip", deviceIp);
		cmdbParam.addQueryParam("is_cn", true);
		return cmdbParam;
	}
}
