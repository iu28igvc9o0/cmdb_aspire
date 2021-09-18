package com.aspire.ums.cmdb.allocate.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

 
//ip分配网段

@Data
@NoArgsConstructor
public class AllocateNetsegment {

	/**
	 *  网段 ID
	 */
	private String id;
	
	/**
     * 承载网络  0-承载网, 1-公网
     */
    private String hostnet;
	
    /**
     * 域名id
     */
    private String domainId;
    
    /**
     * 网段
     */
    private String networkSegment;
    
     
    
}
