package com.aspire.mirror.composite.service.configManagement.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedViewPayload {
	
	private static final long serialVersionUID = -1123424425270983461L;
	
	private String id;

	/** 创建人 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	
	/** 系统id */
	private String  systemId ;
	
	
	private Date createTime;
	
	private String name;
	
	private String describe;
	
	
	private String content;

	private String pageType;

}
