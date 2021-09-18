package com.aspire.mirror.composite.service.configManagement.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * monitor_moduleCustomized新增对象类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.configManagement.entity   
 * 类名称:     ModuleCustomized
 * 类描述:     monitor_actions创建响应对象
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomizedPayload implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -1123424425270983461L;

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
