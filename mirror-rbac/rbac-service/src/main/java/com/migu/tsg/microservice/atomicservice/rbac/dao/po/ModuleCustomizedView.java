package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

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
public class ModuleCustomizedView implements Serializable{
	
	
	private String id;
	
	
	/** 用户 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 首页名称 */
	@JsonProperty("name")
	private String name;
	
	/**模块**/
	@JsonProperty("module_id")
	private String moduleId;
	
	@JsonProperty("create_time")
	private Date createTime;
	
	private String describe;
	
	private String content;
	
	/** 系统id */
	private String  systemId ;

	private String pageType;
	
	

}
