package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     User
 * 类描述:    持久类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -5609418662989180560L;

	/** 主键 */
	private String uuid;
	
	/** 姓名 */
	private String name;
	
	/** 用户类型 */
	private Integer userType;
	
	/**
	 * 头像
	 */
	private String picture;
	
	/** 所属部门 */
	private String deptId;
	
	/** 人员编号 */
	private String no;
	
	/** 性别 */
	private Integer sex;
	
	/** 邮箱 */
	private String mail;
	
	/** 地址 */
	private String address;
	
	/** 电话号码 */
	private String phone;
	
	/** 移动号码 */
	private String mobile;
	
	/** 账号id */
	private String ldapId;
	
	/** 空间 */
	private String namespace;

	/** 人员代码 */
	private String code;
	/** 传真 */
	private String fax;
		/** 职责 */
	private String post;
		/** 关联人 */	
	private String relationPerson;
		/** 描述 */
	private String descr;

	/**
	 * 密码更新时间
	 */
	private Timestamp ldapPasswordUpdatetime;

	/**
	 * 账号状态
	 */
	private String ldapStatus;

	private List<Department> deptList;

	private String deptIdString;

	private List<String> deptIds;
}
