package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SoftwareMaintenance implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	private String project;  //项目
	private String classify;  //分类
	private String softwareName;  //软件名称
	private String unit;  //单位 
	private Integer engineeringNumber;//工程数量
	private Integer number;  //现网数量 
	private String configuration;  //配置 
 	private String vendor;  //厂商 
 	private String authorizationFile;  //授权文件 
	private String outOfDate;  //出保时间
	private String remark;  //备注
	
}
