package com.aspire.ums.cmdb.sync.entity;

import java.util.List;




public class Business {
	private static final long serialVersionUID = -7987818587719372723L;
	private String id;		// 编号
 	private String name;
	private Business parent;
	private String parentIds; // 所有父级编号
// 	private List<Business> childList = Lists.newArrayList();// 拥有子机构列表
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	protected String remarks;	// 备注
	private String businessCode;//业务代码
	private String parentCode;//父业务编码
 	
	

}
