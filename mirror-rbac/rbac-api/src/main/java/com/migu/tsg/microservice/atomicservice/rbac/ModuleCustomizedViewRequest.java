package com.migu.tsg.microservice.atomicservice.rbac;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedViewRequest {
	
	private String id;
	
	/** 用户 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	
	private Date createTime;
	
	
	private String content;

}
