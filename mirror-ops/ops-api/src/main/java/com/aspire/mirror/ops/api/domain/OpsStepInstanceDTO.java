/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsStepInstance.java 
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsStepInstance
 * <p/>
 *
 * 类功能描述: 作业步骤点运行实例
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
@Data
@JsonInclude(Include.NON_NULL)
public class OpsStepInstanceDTO implements Comparable<OpsStepInstanceDTO> {
	
	@JsonProperty("step_instance_id")
	protected Long					stepInstanceId;

	@JsonProperty("step_id")
	protected Long					stepId;

	@JsonProperty("pipeline_instance_id")
	protected Long					pipelineInstanceId;

	@JsonProperty("name")
	protected String				stepInstanceName;

	@JsonProperty("ops_type")
	protected Integer				opsType;						// ops类型: 0  脚本执行    1  文件下发   2 结果文件存储

	@JsonProperty("ord")
	protected Integer				ord;							// 全局顺序

	@JsonProperty("block_ord")
	protected Integer				blockOrd;						// 块顺序

	@JsonProperty("block_name")
	protected String				blockName;

	@JsonProperty("target_ops_user")
	protected String				targetOpsUser;

	@JsonProperty("target_hosts")
	protected List<String>			targetHosts	= new ArrayList<>();

	@JsonProperty("target_host_list")
	protected List<SimpleAgentHostInfo> targetHostList;

	@JsonProperty("bad_hosts")
	protected List<String>			badHosts	= new ArrayList<>();

	@JsonProperty("script_content")
	protected String				scriptContent;

	@JsonProperty("script_content_type")
	protected Integer				scriptContentType;				// 脚本内容类型 1 sh 2 bat 3 python

	@JsonProperty("script_param")
	protected String				scriptParam;
	
	@JsonProperty("script_sudo")
	protected Integer				scriptSudo;						// 是否使用sudo执行脚本

	@JsonProperty("ops_timeout")
	protected Integer				opsTimeout;

	@JsonProperty("file_source")
	protected List<OpsFileSource>	fileSource;

	@JsonProperty("file_target_path")
	protected String				fileTargetPath;
	
	@JsonProperty("file_store_source")
	protected List<String>			fileStoreSource;				// 结果文件来源

	@JsonProperty("file_type")
	protected Integer fileType;

	@JsonProperty("tip_text")
	protected String				tipText;

	@JsonProperty("operator")
	protected String				operator;

	@JsonProperty("status")
	protected Integer				status;

	@JsonProperty("aspnode_result")
	private Integer	aspNodeResult;		// 从脚本执行结果中提取出来的  aspnode_result 标记位         0: 成功     1: 失败


	@JsonProperty("start_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					startTime;

	@JsonProperty("end_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					endTime;

	@JsonProperty("total_time")
	protected Float					totalTime;

	@JsonProperty("total_hosts_num")
	protected Integer				totalHostsNum;

	@JsonProperty("bad_hosts_num")
	protected Integer				badHostsNum;

	@JsonProperty("run_hosts_num")
	protected Integer				runHostsNum;

	@JsonProperty("success_hosts_num")
	protected Integer				successHostsNum;

	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					createTime;

	@JsonProperty("pause_flag")
	protected Integer				pauseFlag;		// 完成后是否暂停标记位   参考常亮    OpsStepDTO.NO_PAUSE|OpsStepDTO.PAUSE

	@JsonProperty("param_sensive_flag")
	protected Integer				paramSensiveFlag;
	
	@JsonProperty("base64_encode")
	private boolean		base64Encode	= false;	// 是否 base64加密, 默认必须为false

	@JsonProperty("auth_list")
	protected List<String> authList;

	@JsonProperty("ops_param_code")    //自定义参数
	protected String opsParamCode;

	@JsonProperty("ops_param_reference_list")
	protected List<OpsParamReference> opsParamReferenceList;

	@JsonProperty("package_password")   //加密参数产出密码设置
	protected String packagePassword;

	@JsonProperty("target_exec_object")
	protected List<TargetExecObject> targetExecObject;

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
	
	@Override
	public int compareTo(OpsStepInstanceDTO other) {
		if (this.getPipelineInstanceId() == null
				|| this.getBlockOrd() == null 
				|| this.getOrd() == null) {
			return 1;
		}
		if (other.getPipelineInstanceId() == null
				|| other.getBlockOrd() == null 
				|| other.getOrd() == null) {
			return -1;
		}
		long result = this.getPipelineInstanceId() - other.getPipelineInstanceId();
		if (result != 0) {
			return (int)result;
		}
		result = this.getBlockOrd() - other.getBlockOrd();
		if (result != 0) {
			return (int)result;
		}
		return this.getOrd() - other.getOrd();
	}
}
