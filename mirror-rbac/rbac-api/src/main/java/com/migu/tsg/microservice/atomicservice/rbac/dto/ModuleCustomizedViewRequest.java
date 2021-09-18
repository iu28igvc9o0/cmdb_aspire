package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedViewRequest {
	
	/** 主键id */
	private String id;
	
	/** 创建者 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	/** 创建时间 */
	private Date createTime;
	
	
	/** 视图名称 */
	@JsonProperty("name")
	private  String name;
	
	/** 描述 */
	private String describe;
	
	/** 组件详细信息 */
	private String content;
	
	/** 系统id */
	private String  systemId ;

	private String pageType;

}
