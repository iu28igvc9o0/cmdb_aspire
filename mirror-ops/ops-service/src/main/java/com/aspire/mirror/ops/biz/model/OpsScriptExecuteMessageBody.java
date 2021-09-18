package com.aspire.mirror.ops.biz.model;

import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: OpsScriptExecuteMessageBody
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
public class OpsScriptExecuteMessageBody extends AbstractOpsMessageBody {
	@JsonProperty("script")
	private Script script;
	
	@Data
	public static class Script {
		@JsonProperty("content_type")
		private Integer	contentType;	// 脚本内容类型 1 sh 2 bat 3 python

		@JsonProperty("script_content")
		private String	scriptContent;
		
		@JsonProperty("script_params")
		private String scriptParams;
		
		@JsonProperty("script_sudo")
		private Integer scriptSudo;		// 是否sudo执行脚本     0 不使用     1 使用

		@JsonProperty("encode")
		private String	encode;
		
		@JsonProperty("timeout")
		private Integer timeout;	
		
		public void setEncodeScriptContent(String rawContent, String encode) {
			this.encode = encode;
			scriptContent = rawContent;
			if (EncryptUtil.ALGORITHM_BASE64.equals(encode)) {
				scriptContent = EncryptUtil.base64Encrypt(scriptContent);
			} 
			else if (EncryptUtil.ALGORITHM_AES.equals(encode)) {
				scriptContent = EncryptUtil.aesEncrypt(scriptContent);
			}
		}
	}
}
