/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  YumSourceModel.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsYumFileModel
 * <p/>
 *
 * 类功能描述: yum源文件
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OpsYumFileModel extends OpsGroupObject{
	public static Integer FILE_TYPE_YUM_SOURCE = 0;
	public static Integer FILE_TYPE_YUM_CONFIG = 1;
	
	protected Long		id;
	protected Integer 	fileType;			// FILE_TYPE_YUM_SOURCE | FILE_TYPE_YUM_CONFIG
	protected String	name;
	protected String	version;
	protected Integer	osDistributionId;
	protected String	osDistributionName;
	protected String	uploadFilePath;
	protected Long		yumFileGroupId;
	protected String	yumFileGroupName;
	protected String	creater;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		createTime;
	protected String	lastUpdater;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		lastUpdateTime;
	protected String 	description;
	
	@Data
	public static class DownFileParam {
		@JsonProperty("file_path")
		private String filePath;
	}
}
