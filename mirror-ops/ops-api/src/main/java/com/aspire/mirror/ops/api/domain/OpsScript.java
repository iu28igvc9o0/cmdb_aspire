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
import java.util.List;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@EqualsAndHashCode(of= {"scriptName"})
@ToString(of={"pipelineName", "scriptName"})
@JsonInclude(Include.NON_NULL)
public class OpsScript extends OpsGroupObject{
	@JsonProperty("script_id")
	protected Long		scriptId;

	@JsonProperty("script_name")
	protected String	scriptName;

	/**
	 * 脚本使用描述
	 */
	@JsonProperty("script_use_desc")
	protected String scriptUseDesc;

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

	@JsonProperty("ops_param_code")    //自定义参数
	protected String opsParamCode;

	@JsonProperty("ops_param_reference_list")
	protected List<OpsParamReference> opsParamReferenceList;

	@JsonProperty("package_password")   //加密参数产出密码设置
	protected String packagePassword;

	@JsonProperty("audit_status")
	private String auditStatus;

	@JsonProperty("audit_desc")
	private String auditDesc;

	@JsonProperty("reviewer")
	private String reviewer;

	@JsonProperty("review_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reviewTime;

	@JsonProperty("current_version")
	private String currentVersion;

	@JsonProperty("creater_dept_id")
	private String createrDeptId;

	private Boolean needAddHis = true;
	public String encodeScriptContent() {
		if (base64Encode) {
			return this.scriptContent;
		}
		this.base64Encode = true;
		if (this.scriptContent != null) {
			this.scriptContent = EncryptUtil.base64Encrypt(this.scriptContent);
		}
		return this.scriptContent;
	}
	
	public String decodeScriptContent() {
		if (!base64Encode) {
			return this.scriptContent;
		}
		this.base64Encode = false;
		if (this.scriptContent != null) {
			this.scriptContent = EncryptUtil.base64Decrypt(this.scriptContent);
		}
		return this.scriptContent;
	}
	public String newVersion() {
		Date now = new Date();
		return "V" + DateUtil.format(now, DateUtil.DATE_TIME_FORMAT);
	}
}
