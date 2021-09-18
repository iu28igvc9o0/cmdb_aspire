package com.aspire.ums.cmdb.sync.entity;

import java.util.List;


//import com.google.common.collect.Lists;

public class Area {
	
	private static final long serialVersionUID = 1L;
	private Area parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
//	private List<Office> officeList = Lists.newArrayList(); // 部门列表
//	private List<Area> childList = Lists.newArrayList();	// 拥有子区域列表

	public Area(){
		super();
	}
	
	
}
