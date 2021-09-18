package com.aspire.ums.cmdb.allocate.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AllocateDomainResp {
	
	 
	/**
	 * ID
	 */
	@JsonProperty("id")
	private String id;
    /**
     * 业务系统id
     */
	@JsonProperty("system_id")
    private String systemId;
    
    /**
     * 承载网络  0-承载网, 1-公网
     */
	@JsonProperty("hostnet")
    private String hostnet;
    
    /**
     * 域名
     */
	@JsonProperty("domain")
    private String domain;
	
    /**
     * 联系人姓名
     */
	@JsonProperty("user_name")
    private String userName;
    
    /**
     * 电话
     */
	@JsonProperty("telephone")
    private String telephone;
	
    /**
     * 描述说明
     */
	@JsonProperty("description")
    private String description;

	 
	/**
     * 网段
     */
	@JsonProperty("network_segment")
    private String networkSegment;
	
	
	

}
