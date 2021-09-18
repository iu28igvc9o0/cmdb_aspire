package com.aspire.ums.cmdb.allocate.util;

 

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AllocateNetSegmentResp {
	
	
	/**
	 *  网段 ID
	 */
	@JsonProperty("id")
	private String id;
	
	/**
     * 承载网络  0-承载网, 1-公网
     */
	@JsonProperty("hostnet")
    private String hostnet;
	
    /**
     * 域名id
     */
	@JsonProperty("domain_id")
    private String domainId;
    
    /**
     * 网段
     */
	@JsonProperty("network_segment")
    private String networkSegment;
	


}
