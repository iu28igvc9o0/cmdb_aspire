package com.aspire.ums.cmdb.sync.entity;

import java.util.List;



public class Role {
	
	private static final long serialVersionUID = 1L;
	private Office office;	// 归属机构
	private String name; 	// 角色名称
	private String dataScope; // 数据范围
	
//	private List<User> userList = Lists.newArrayList(); // 拥有用户列表
//	private List<Office> officeList = Lists.newArrayList(); // 按明细设置数据范围
//	private List<Business> businessList = Lists.newArrayList();//拥有业务数据

	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	public static final String DATA_SCOPE_COMPANY = "3";
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	public static final String DATA_SCOPE_OFFICE = "5";
	public static final String DATA_SCOPE_SELF = "8";
	public static final String DATA_SCOPE_CUSTOM = "9";

}
