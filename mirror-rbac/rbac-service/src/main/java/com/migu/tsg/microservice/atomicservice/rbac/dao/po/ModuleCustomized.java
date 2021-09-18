package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.entity   
 * 类名称:     ModuleCustomized
 * 类描述:    持久类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCustomized implements Serializable{
	/** 用户 */
	private String userId;
	
	/** 模块 */
	private String moduleId;
	
	/** json内容 */
	private String content;
	
}
