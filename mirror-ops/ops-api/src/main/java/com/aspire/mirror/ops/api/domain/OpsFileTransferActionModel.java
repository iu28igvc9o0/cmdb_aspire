package com.aspire.mirror.ops.api.domain;

import java.util.List;

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
 * 类名: OpsFileTransferActionModel
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
public class OpsFileTransferActionModel extends AbstractOpsActionModel {
	
	@JsonProperty("transfer_files")
	private List<OpsFileSource>	fileSourceList;

	@JsonProperty("target_file_path")
	private String				targetFilePath;

	@JsonProperty("file_type")
	protected Integer fileType;
}
