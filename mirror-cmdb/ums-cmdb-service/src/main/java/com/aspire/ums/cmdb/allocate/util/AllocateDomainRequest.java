package com.aspire.ums.cmdb.allocate.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 域名操作请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.allocate.util
 * 类名称:    AllocateDomainRequest.java
 * 类描述:    域名操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AllocateDomainRequest {
    

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
