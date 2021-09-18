/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsFileSource.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月1日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsFileSource
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月1日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OpsFileSource {
	public static final String	FILE_TYPE_UPLOAD	= "upload";
	public static final String	FILE_TYPE_AGENT		= "agent";

	@JsonProperty("file_type")
	private String				fileType;						// upload | agent
	
	@JsonProperty("file_path")
	private String				filePath;

	@JsonProperty("source_agent_list")
	private List<String>		sourceAgentList;

	@JsonProperty("source_ops_user")
	private String				sourceOpsUser;
}
