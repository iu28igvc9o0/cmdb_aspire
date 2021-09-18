/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsStep.java 
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
import java.util.Map;

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
 * 类名: OpsStepDTO
 * <p/>
 *
 * 类功能描述: 流水作业步骤定义
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
public class OpsStepDTO {
	public static final int			NO_PAUSE					= 0;	// 自然执行
	public static final int			PAUSE						= 1;	// 完成后暂停
	public static final int			OPS_TYPE_SCRIPT				= 0;	// 脚本执行
	public static final int			OPS_TYPE_FILE_TRANSFER		= 1;	// 文件分发
	public static final int			OPS_TYPE_RESULT_FILE_STORE	= 2;	// 结果文件存储
	@JsonProperty("step_id")
	protected Long					stepId;
	
	@JsonProperty("pipeline_id")
	protected Long					pipelineId;
	
	@JsonProperty("name")
	protected String				stepName;
	
	@JsonProperty("ops_type")
	protected Integer				opsType;						// ops类型: 0 脚本执行 1 文件下发 2 结果文件存储
	
	@JsonProperty("ord")
	protected Integer				ord;							// 全局顺序
	
	@JsonProperty("block_ord")
	protected Integer				blockOrd;						// 块顺序
	
	@JsonProperty("block_name")
	protected String				blockName;
	
	@JsonProperty("target_ops_user")
	protected String				targetOpsUser;
	
	@JsonProperty("target_hosts")
	protected List<String>			targetHosts;

	@JsonProperty("target_host_list")
	protected List<SimpleAgentHostInfo> targetHostList;

	@JsonProperty("script_id")
	protected Long					scriptId;						// 执行的脚本id
	
	@JsonProperty("embed_script")
	protected OpsScript 			embedScript;					// 新增和修改时, 对应的脚本请求实体		
	
	@JsonProperty("script_param")
	protected String				scriptParam;
	
	@JsonProperty("script_sudo")
	protected Integer				scriptSudo;						// 是否使用sudo执行脚本
	
	@JsonProperty("ops_timeout")
	protected Integer				opsTimeout;
	
	@JsonProperty("file_source")
	protected List<OpsFileSource>	fileSource;						// 文件分发来源
	
	@JsonProperty("file_target_path")
	protected String				fileTargetPath;					// 文件分发目标路径

	@JsonProperty("file_type")
	protected Integer fileType;

	@JsonProperty("file_store_source")
	protected List<String>			fileStoreSource;				// 结果文件来源

	@JsonProperty("file_store_converge_type")
	protected Integer fileStoreConvergeType;		//文件结果汇聚类型  0：不汇聚（默认）  1：追加汇聚（同文件名内容追加汇总）2：分类汇聚（按文件类型分类汇聚，暂按特定规则处理基线shadow文件）

	@JsonProperty("file_store_safety")
	protected Integer fileStoreSafety;  // 0 不安全  1安全

	@JsonProperty("tip_text")
	protected String				tipText;						// 当前step被暂停时, 用户手动执行时的提示信息
	
	@JsonProperty("creater")
	protected String				creater;
	
	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					createTime;
	
	@JsonProperty("updater")
	protected String				updater;
	
	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					updateTime;
	
	@JsonProperty("pause_flag")
	protected Integer				pauseFlag;						// 完成后是否暂停标记位   参考常亮    NO_PAUSE|PAUSE
	
	@JsonProperty("param_sensive_flag")
	protected Integer				paramSensiveFlag;				// 参数敏感标记位   0 不敏感 1 敏感

	@JsonProperty("replace_attrs")
	protected List<ReplaceAttrDefine> replaceAttrList ;

	@JsonProperty("target_exec_object")
	protected List<TargetExecObject> targetExecObject;

	@Data
	public static class ReplaceAttrDefine {
		public static final String REPLACE_TYPE_APPEND 	= "A";
		public static final String REPLACE_TYPE_REPLACE = "R";
		
		@JsonProperty("replace_attr")
		private String replaceAttr;
		
		@JsonProperty("replace_type")
		private String replaceType;			// 替换类型    A|R   A:append 追加        R:replace 完全替换  
	}

}
