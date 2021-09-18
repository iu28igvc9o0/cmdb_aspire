package com.aspire.mirror.ops.biz.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.aspire.mirror.ops.api.domain.OpsFileSource;
import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.aspire.mirror.ops.biz.CommonSftpServerConfig;
import com.aspire.mirror.ops.biz.model.OpsFileTransferMessageBody.AgentSourceFile;
import com.aspire.mirror.ops.biz.model.OpsFileTransferMessageBody.UploadFiles;
import com.aspire.mirror.ops.biz.model.OpsScriptExecuteMessageBody.Script;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.OpsStep;
import com.aspire.mirror.ops.domain.OpsStepInstance;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: OpsMessage
 * <p/>
 *
 * 类功能描述: 参考如下消息格式  
 * <pre>
 *   {
 *    	"meta": {
 *    		"step_instance_id": 1,
 *    		"step_id": 1,
 *    		"pipeline_instance_id": 1,
 *    		"ops_type": 0   // step类型:  0 脚本执行  1  文件分发
 *    	}, 
 *    	
 *    	// 脚本操作
 *    	"ops_script": {
 *         "target_ops_user": "aspire",
 *    		"target_host_list": [
 *    			{
 *    				"ip_address": "10.12.70.63",
 *    				"proxy_id": 1,
 *    				"ssh_user": "test",
 *    				"ssh_pass": "test"
 *    			}, 
 *    			{
 *    				"ip_address": "10.12.70.65",
 *    				"proxy_id": 1,
 *    				"ssh_user": "test",
 *    				"ssh_pass": "test"
 *    			}
 *    		],
 *    		"script": {
 *    			"target_ops_user": "aspire",
 *    			"content_type": "1",   			// 脚本内容类型   1 sh 2 bat 3 python
 *    			"script_content": "xxx",
 *    			"encode": "base64"
 *    		},
 *    	}, 
 *    	
 *    	// 文件分发
 *    	"ops_file_transfer": {
 *    		"target_ops_user": "aspire",
 *    		"target_file_path": "/var/lib",
 *    		"target_host_list": [
 *    			{
 *    				"ip_address": "10.12.70.11",
 *    				"proxy_id": 1,
 *    				"ssh_user": "test",
 *    				"ssh_pass": "test"
 *    			}, 
 *    			{
 *    				"ip_address": "10.12.70.12",
 *    				"proxy_id": 1,
 *    				"ssh_user": "test",
 *    				"ssh_pass": "test"
 *    			}
 *    		], 
 *    		// 本地上传
 *    		"upload_files": {
 *    			"sftp_server": "10.12.70.63",
 *    			"sftp_user": "aspire",
 *    			"sftp_pass": "pass"
 *    			"file_list": ["/home/aspire/sftp/alert-service.jar", "/home/aspire/sftp/composite-service.jar"]
 *    		},
 *    		// 远程主机文件分发
 *    		"agent_files": [
 *    			{
 *    				"source_ops_user": "spider",
 *    				"source_file_path": "/opt/spider.tar",
 *    				"source_host_list": [
 *    					{
 *    						
 *    						"ip_address": "10.12.70.43",
 *    						"proxy_id": 1,
 *    						"ssh_user": "test",
 *    						"ssh_pass": "test"
 *    					}, 
 *    					{
 *    						"ip_address": "10.12.70.44",
 *    						"proxy_id": 1,
 *    						"ssh_user": "test",
 *    						"ssh_pass": "test"
 *    					}
 *    				]
 *    			},
 *    			{
 *    				"source_ops_user": "kafka",
 *    				"source_file_path": "/opt/kafka.rpm",
 *    				"source_host_list": [
 *    					{
 *    						
 *    						"ip_address": "10.12.70.40",
 *    						"proxy_id": 1,
 *    						"ssh_user": "test",
 *    						"ssh_pass": "test"
 *    					}, 
 *    					{
 *    						"ip_address": "10.12.70.41",
 *    						"proxy_id": 1,
 *    						"ssh_user": "test",
 *    						"ssh_pass": "test"
 *    					}
 *    				]
 *    			}
 *    		]
 *    	}
 *   }
 * </pre>
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月23日  
 *
 * @version	V1.0 
 * 
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OpsMessage {
	@JsonProperty("meta")
	private OpsMessageMeta				meta;

	@JsonProperty("ops_script")
	private OpsScriptExecuteMessageBody	script;

	@JsonProperty("ops_file_transfer")
	private OpsFileTransferMessageBody			fileTransfer;
	
	@JsonProperty("ops_file_store")
	private OpsResultFileStoreMessageBody		fileStore;
	
	
	public static OpsMessage generateOpsMessage(
			OpsStepInstance stepInstance, Map<String, AgentHostInfo> agentDetailMap, CommonSftpServerConfig sftpConfig, Map<String, Map<String, Pair<String, String>>> customizeParams) {
		return generateOpsMessage(stepInstance, agentDetailMap, sftpConfig, null, customizeParams);
	}
	
	public static OpsMessage generateOpsMessage(OpsStepInstance stepInstance, 
			Map<String, AgentHostInfo> agentDetailMap, CommonSftpServerConfig sftpConfig, OpsMessageExtendMeta extendMeta, Map<String, Map<String, Pair<String, String>>> customizeParams) {
		OpsMessage message = new OpsMessage();
		OpsMessageMeta meta = new OpsMessageMeta();
		meta.setStepId(stepInstance.getStepId());
		meta.setStepInstanceId(stepInstance.getStepInstanceId());
		meta.setPipelineInstanceId(stepInstance.getPipelineInstanceId());
		meta.setOpsType(stepInstance.getOpsType());
		if (extendMeta != null) {
			meta.setExtendMeta(extendMeta);
		}
		message.setMeta(meta);
		List<OpsAgentInfo> targetAgentList
				= getAgentListByConcatProxyIdAndIp(agentDetailMap, stepInstance.getTargetHosts());
		if (OpsStep.OPS_TYPE_SCRIPT == stepInstance.getOpsType()) {
			OpsScriptExecuteMessageBody scriptBody = new OpsScriptExecuteMessageBody();
			scriptBody.setTargetOpsUser(stepInstance.getTargetOpsUser());
			scriptBody.setTargetHostList(targetAgentList);
			scriptBody.setAuthList(stepInstance.getAuthList());
			scriptBody.setCustomizeParams(customizeParams);
			Script script = new OpsScriptExecuteMessageBody.Script();
			script.setContentType(stepInstance.getScriptContentType());
			script.setEncodeScriptContent(stepInstance.getScriptContent(), EncryptUtil.ALGORITHM_AES);
			script.setScriptParams(stepInstance.getScriptParam());
			script.setScriptSudo(stepInstance.getScriptSudo());
			script.setTimeout(stepInstance.getOpsTimeout());
			scriptBody.setScript(script);
			message.setScript(scriptBody);
		} 
		else if (OpsStep.OPS_TYPE_FILE_TRANSFER == stepInstance.getOpsType()) {
			OpsFileTransferMessageBody transfer = new OpsFileTransferMessageBody();
			transfer.setTargetHostList(targetAgentList);
			transfer.setTargetOpsUser(stepInstance.getTargetOpsUser());
			transfer.setTargetFilePath(stepInstance.getFileTargetPath());
			
			Pair<UploadFiles, List<AgentSourceFile>> filePair 
					= buildTransferFileDataModel(stepInstance.getFileSource(), agentDetailMap, sftpConfig);
			transfer.setUploadFiles(filePair.getLeft());
			transfer.setAgentSourceFiles(filePair.getRight());
			message.setFileTransfer(transfer);
		}
		else if (OpsStep.OPS_TYPE_RESULT_FILE_STORE == stepInstance.getOpsType()) {
			OpsResultFileStoreMessageBody fileStore = new OpsResultFileStoreMessageBody();
			fileStore.setTargetHostList(targetAgentList);
			fileStore.setTargetOpsUser(stepInstance.getTargetOpsUser());
			fileStore.setSftpConfig(sftpConfig);
			fileStore.setFileStoreSource(stepInstance.getFileStoreSource());
			message.setFileStore(fileStore);
		}
		return message;
	}
	
	private static List<OpsAgentInfo> getAgentListByConcatProxyIdAndIp(
			Map<String, AgentHostInfo> agentDetailMap, List<String> concatList) {
		return concatList.parallelStream().map(concatIp -> {
			return OpsAgentInfo.from(agentDetailMap.get(concatIp));
		}).collect(Collectors.toList());
	}
	
	private static Pair<UploadFiles, List<AgentSourceFile>> buildTransferFileDataModel(
			List<OpsFileSource> fileSources, Map<String, AgentHostInfo> agentDetailMap, CommonSftpServerConfig sftpConfig) {
		UploadFiles uploadFileData = null;
		List<AgentSourceFile> agentFileData = null;
		if (CollectionUtils.isEmpty(fileSources)) {
			return Pair.of(uploadFileData, agentFileData);
		}
		
		List<OpsFileSource> uploadFiles = fileSources.stream().filter(file -> {
			return OpsFileSource.FILE_TYPE_UPLOAD.equals(file.getFileType());
		}).collect(Collectors.toList());
		
		if (CollectionUtils.isNotEmpty(uploadFiles)) {
			List<String> filePathList = uploadFiles.stream().map(file -> {
				return sftpConfig.getRootDirectory() + file.getFilePath();
			}).collect(Collectors.toList());
			uploadFileData = new UploadFiles();
			uploadFileData.setSftpServer(sftpConfig.getIpAddress());
			uploadFileData.setSftpPort(sftpConfig.getPort());
			uploadFileData.encodeSftpUserInfo(EncryptUtil.ALGORITHM_AES, sftpConfig.getLoginUser(), sftpConfig.getLoginPass());
			uploadFileData.setFileList(filePathList);
		}
		
		List<OpsFileSource> agentFiles = fileSources.stream().filter(file -> {
			return OpsFileSource.FILE_TYPE_AGENT.equals(file.getFileType());
		}).collect(Collectors.toList());
		
		agentFileData = agentFiles.stream().map(file -> {
			AgentSourceFile agentFile = new AgentSourceFile();
			agentFile.setSourceFilePath(file.getFilePath());
			agentFile.setSourceOpsUser(file.getSourceOpsUser());
			List<OpsAgentInfo> agentHostList = file.getSourceAgentList().stream().map(agentId -> {
				return OpsAgentInfo.from(agentDetailMap.get(agentId));
			}).collect(Collectors.toList());
			agentFile.setSourceAgentList(agentHostList);
			return agentFile;
		}).collect(Collectors.toList());
		
		return Pair.of(uploadFileData, agentFileData);
	}
}
