package com.migu.tsg.microservice.atomicservice.rbac.entity;

import lombok.Data;

@Data
public class EipDeptDTO {

	// "sort": "8005",排序
	// "timestamp": "000000000313AB4A",时间戳
	// "fullName": "卓望公司/卓望信息/财务部",部门全称
	// "parentId": "D01600",父级部门ID
	// "fullId": "D00001,D01600,D00005",部门全称ID
	// "deptName": "财务部",部门名称
	// "deptId": "D00005",部门ID

	private String sort;
	private String timestamp;
	private String fullName;
	private String parentId;
	private String fullId;
	private String deptName;
	private String deptId;
}
