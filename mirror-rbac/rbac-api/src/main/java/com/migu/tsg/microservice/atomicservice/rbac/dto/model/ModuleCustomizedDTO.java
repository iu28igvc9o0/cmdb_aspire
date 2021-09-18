package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.dto.model   
 * 类名称:     ModuleCustomized
 * 类描述:    业务类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ModuleCustomizedDTO implements Serializable{
	/** 用户 */
	 @ApiModelProperty(value = "用户")
	 @JsonProperty("user_id")
	private String userId;
	
	/** 模块 */
	 @ApiModelProperty(value = "模块")
	 @JsonProperty("module_id")
	private String moduleId;
	
	/** json内容 */
	 @ApiModelProperty(value = "json内容")
	 @JsonProperty("content")
	private String content;
	
}
