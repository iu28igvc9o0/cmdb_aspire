package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_department新增对象类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     Department
 * 类描述:     monitor_actions分页查询对象
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
 
@Data
@NoArgsConstructor
public class DepartmentPageRequest{

    /**
    * 每页显示记录条数
    */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
    * 第几页
    */
    @JsonProperty("page_no")
    private Integer pageNo;

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
	
}
