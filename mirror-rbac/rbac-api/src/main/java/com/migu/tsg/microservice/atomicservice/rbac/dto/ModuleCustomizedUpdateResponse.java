package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//import lombok.AllArgsConstructor;

/**
 * monitor_moduleCustomized修改对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.entity   
 * 类名称:     ModuleCustomized
 * 类描述:    monitor_moduleCustomized修改响应对象
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
@Data
@NoArgsConstructor
public class ModuleCustomizedUpdateResponse implements Serializable{
	/** 用户 */
	@JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	@JsonProperty("module_id")
	private String moduleId;
	
	/** json内容 */
	@JsonProperty("content")
	private String content;
	
}
