package com.aspire.ums.cmdb.resource.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CmdbBusiness implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 300297746261108014L;
	
	private String id;
	private String businessName;
	private String businessId;
	private String parentId;
	private String businessGroup;
	private String businessCode;
	
	private CmdbBusiness parent;
	
	private String delFlag;
	
	private String disabled;
	

	private List<CmdbBusiness> childList = Lists.newArrayList();// 拥有子业务列表

}
