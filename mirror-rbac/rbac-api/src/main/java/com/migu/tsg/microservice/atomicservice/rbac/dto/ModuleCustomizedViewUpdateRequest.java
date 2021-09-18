package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedViewUpdateRequest {
	
	private String id;
	
	/** 用户 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	
	/** 系统id */
	private String  systemId ;
	
	
	private String name;
	
	
	
	private String describe;
	
	
	private String content;

	private String pageType;

}
