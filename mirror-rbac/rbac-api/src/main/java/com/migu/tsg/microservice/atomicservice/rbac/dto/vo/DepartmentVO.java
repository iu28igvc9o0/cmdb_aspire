package com.migu.tsg.microservice.atomicservice.rbac.dto.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.dto.vo   
 * 类名称:     Department
 * 类描述:     数据模型
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
 
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVO implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 6075544918383087758L;

	/** id */
	 @JsonProperty("uuid")
	private String uuid;
	
	/** 父节点id */
	 @JsonProperty("parent_id")
	private String parentId;
	
	/** 部门名称 */
	 @JsonProperty("name")
	private String name;
	
	/** 部门编号 */
	 @JsonProperty("no")
	private String no;
	
	/** 部门类型 1:正式部门 2：临时部门*/
	 @JsonProperty("dept_type")
	private Integer deptType;
	
	/** 部门描述 */
	 @JsonProperty("descr")
	private String descr;
	
	/** 部门层级 */
	 @JsonProperty("level")
	private Integer level;
	
	/** 空间 */
	 @JsonProperty("namespace")
	private String namespace;
	
}
