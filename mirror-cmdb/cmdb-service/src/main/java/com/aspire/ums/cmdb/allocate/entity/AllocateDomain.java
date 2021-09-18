package com.aspire.ums.cmdb.allocate.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

 
//ip分配域名

@Data
@NoArgsConstructor
public class AllocateDomain { 

	/**
	 * ID
	 */
	private String id;
    /**
     * 业务系统id
     */
    private String systemId;
    
    /**
     * 承载网络  0-承载网, 1-公网
     */
    private String hostnet;
    
    /**
     * 域名
     */
    private String domain;
	
    /**
     * 联系人姓名
     */
    private String userName;
    
    /**
     * 电话
     */
    private String telephone;
	
    /**
     * 描述说明
     */
    private String description;
    
    /**
     * 网段
     */
    private String netSegment; 
    /**
     * 已分配数
     */
    private String sum;
    
     
    
}
