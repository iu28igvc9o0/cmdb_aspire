package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     Department
 * 类描述:    持久类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 4799293751609962978L;

	/** id */
	private String uuid;
	
	/** 父节点id */
	private String parentId;
	
	/** 部门名称 */
	private String name;
	
	/** 部门编号 */
	private String no;
	
	/** 部门类型 */
	private Integer deptType;
	
	/** 部门描述 */
	private String descr;
	
	/** 部门层级 */
	private Integer level;
	
	/** 空间 */
	private String namespace;
	
	/**
	 * 是否顶级
	 * */
	private boolean top=false;
	
}
