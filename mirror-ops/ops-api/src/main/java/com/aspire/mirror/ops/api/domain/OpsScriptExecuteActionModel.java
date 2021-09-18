package com.aspire.mirror.ops.api.domain;

import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: OpsScriptExecuteActionModel
 * <p/>
 *
 * 类功能描述: 脚本操作数据定义对象
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
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class OpsScriptExecuteActionModel extends AbstractOpsActionModel {
	@JsonProperty("script")
	private Script script;
	
	@Data
	public static class Script {
		
		@JsonProperty("content_type")
		private Integer	contentType;					// 脚本内容类型 1 sh 2 bat 3 python

		@JsonProperty("script_content")
		private String	scriptContent;
		
		@JsonProperty("base64_encode")
		private boolean		base64Encode	= false;	// 是否 base64加密, 默认为false
		
		@JsonProperty("script_params")
		private String scriptParams;
		
		@JsonProperty("script_sudo")
		private Integer scriptSudo;						// 是否使用sudo执行      1 ： 使用    0： 不使用
		
		@JsonProperty("param_sensive_flag")
		private Integer paramSensiveFlag;
		
		@JsonProperty("timeout")
		private Integer timeout;

//		@JsonProperty("ops_param_code")    //自定义参数
//		protected String opsParamCode;
		@JsonProperty("ops_param_reference_list")
		protected List<OpsParamReference> opsParamReferenceList;

		@JsonProperty("package_password")   //加密参数产出密码设置
		protected String packagePassword;

		@JsonProperty("script_id")
		protected Long scriptId;

//		@JsonProperty("target_exec_object")
//		protected List<TargetExecObject> targetExecObject;



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
	}
}
