package com.migu.tsg.microservice.atomicservice.rbac.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//import lombok.AllArgsConstructor;

/**
 * 视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.configManagement.dto.vo   
 * 类名称:     ModuleCustomized
 * 类描述:     数据模型
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedVO implements Serializable{
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
