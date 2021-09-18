package com.aspire.mirror.ops.biz.model;

import java.util.List;

import com.aspire.mirror.ops.biz.CommonSftpServerConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsResultFileStoreMessageBody
 * <p/>
 *
 * 类功能描述: 结果文件存储操作数据定义对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class OpsResultFileStoreMessageBody extends AbstractOpsMessageBody {
	
	@JsonProperty("sftp_config")
	private CommonSftpServerConfig sftpConfig;
	
	@JsonProperty("flie_store_source")
	private List<String> fileStoreSource;
}
