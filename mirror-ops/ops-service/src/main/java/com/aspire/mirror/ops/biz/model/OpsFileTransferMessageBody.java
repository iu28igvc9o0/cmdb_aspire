package com.aspire.mirror.ops.biz.model;

import java.util.List;

import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: OpsFileTransferMessageBody
 * <p/>
 *
 * 类功能描述: 文件分发操作数据定义对象
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
public class OpsFileTransferMessageBody extends AbstractOpsMessageBody {
	@JsonProperty("upload_files")
	private UploadFiles				uploadFiles;

	@JsonProperty("agent_files")
	private List<AgentSourceFile>	agentSourceFiles;

	@JsonProperty("target_file_path")
	private String					targetFilePath;
	
	@Setter
	@Getter
	@ToString(exclude="sftpPass")
	public static class UploadFiles {
		@JsonProperty("sftp_server")
		private String			sftpServer;

		@JsonProperty("sftp_user")
		private String			sftpUser;

		@JsonProperty("sftp_pass")
		private String			sftpPass;

		@JsonProperty("encode")
		private String			encode;

		@JsonProperty("sftp_port")
		private Integer			sftpPort;

		@JsonProperty("file_list")
		private List<String>	fileList;
		
		public void encodeSftpUserInfo(String encode, String sshUser, String sshPass) {
			this.encode = encode;
			this.sftpUser = sshUser;
			this.sftpPass = sshPass;
			if (EncryptUtil.ALGORITHM_BASE64.equals(encode)) {
				this.sftpUser = EncryptUtil.base64Encrypt(sshUser);
				this.sftpPass = EncryptUtil.base64Encrypt(sshPass);
			} 
			else if (EncryptUtil.ALGORITHM_AES.equals(encode)) {
				this.sftpUser = EncryptUtil.aesEncrypt(sshUser);
				this.sftpPass = EncryptUtil.aesEncrypt(sshPass);
			}
		}
	}
	
	@Data
	public static class AgentSourceFile {
		@JsonProperty("source_ops_user")
		private String sourceOpsUser;
		
		@JsonProperty("source_file_path")
		private String sourceFilePath;
		
		@JsonProperty("source_host_list")
		private List<OpsAgentInfo> sourceAgentList;
	}
}
