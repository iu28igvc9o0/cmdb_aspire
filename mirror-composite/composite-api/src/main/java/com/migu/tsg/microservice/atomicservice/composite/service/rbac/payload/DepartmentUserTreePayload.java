package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

//import lombok.AllArgsConstructor;

/**
 * @author menglinjie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUserTreePayload implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 6340647027170725597L;

	/** 
	 * @fields serialVersionUID 
	 */ 

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
	
	/** 部门类型 */
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
	 
	private List<DepartmentUserTreePayload> childList;

	private List<UserResponse> userPayloadList;

	private Integer hasChild;
	
}
