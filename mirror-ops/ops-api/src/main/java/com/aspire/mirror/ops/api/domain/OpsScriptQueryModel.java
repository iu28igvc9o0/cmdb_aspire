/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsScript.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsScript
 * <p/>
 *
 * 类功能描述: 脚本
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@Setter
public class OpsScriptQueryModel {
	private String	scriptNameLike;
	private String	scriptContentLike;
	private String	pipelineNameLike;
	private String  groupNameLike;
	@JsonProperty("step_script_flag")
	private String stepScriptFlag; 		// 是否只查询作业下的脚本    Y: 作业下脚本，  N ：非作业下脚本
	
	@JsonProperty("script_id")
	protected Long		scriptId;

	@JsonProperty("script_name")
	protected String	scriptName;

	@JsonProperty("script_content")
	protected String	scriptContent;

	@JsonProperty("base64_encode")
	private boolean		base64Encode	= false;	// 是否 base64加密, 默认必须为false

	@JsonProperty("content_type")
	protected Integer	contentType;				// 脚本内容类型    1 sh 2 bat 3 python  参考ScriptContentTypeEnum

	@JsonProperty("step_id")
	protected Long		stepId;

	@JsonProperty("pipeline_id")
	protected Long		pipelineId;

	@JsonProperty("pipeline_name")
	protected String	pipelineName;

	@JsonProperty("is_public")
	protected Integer isPublic;  	// 0 非公共脚本   1 公共脚本

	@JsonProperty("label_id")
	protected String labelId;  	// label id

	@JsonProperty("label_name")
	protected String labelName;  // label name

	@JsonProperty("creater")
	protected String	creater;

	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		createTime;

	@JsonProperty("updater")
	protected String	updater;

	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		updateTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	createTimeStart;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	createTimeEnd;
	
	@JsonProperty("page_size")
	private Integer	pageSize;		// 如果为null， 则查询全部数据
	@JsonProperty("page_no")
	private Integer	pageNo;			// 从0开始

	@JsonProperty("group_ids")
	private String groupIds;

	@JsonProperty("audit_status")
	private String auditStatus;

	public Integer getStartIdx() {
		if (pageSize == null) {
			return null;
		}
		return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
	}
}
