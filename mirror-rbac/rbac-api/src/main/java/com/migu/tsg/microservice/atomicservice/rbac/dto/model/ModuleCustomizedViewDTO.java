package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ModuleCustomizedViewDTO implements Serializable{
	
	
	private String id;
	
	
	/** 用户 */
	@JsonProperty("user_id")
	private String userId;
	
	private String name;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	
	private Date createTime;
	
	
	private String content;
	
	
	private String describe;
	
	
	/** 系统id */
	private String  systemId ;

	private String pageType;
}
