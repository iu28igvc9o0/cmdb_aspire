package com.aspire.mirror.composite.service.cmdb.payload;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网段分页请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.allocate.util
 * 类名称:    NetSegmentPageRequest.java
 * 类描述:    域名操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class CompAllocateManagePageRequest {
	
	/**
     * 分页页标
     */
    @NotNull
    @JsonProperty("page_size")
    private String pageSize;

    /**
     * 每页数量
     */ 
    @NotNull
    @JsonProperty("page_no")
    private String pageNo;
	
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
     *  ipdress模糊查询
     */
	@JsonProperty("ipadress")
    private String ipadress;
	
	
	/**
     *  域名模糊查询
     */
	@JsonProperty("domain")
    private String domain;
	
	


}
