package com.aspire.ums.cmdb.maintenance.util;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MaintenSoftPageRequest {
    
	 
	// 分页页标
    @JsonProperty("page_size")
    private String pageSize;

    // 每页数量
    @JsonProperty("page_no")
    private String pageNo;
	
   
    // 本期维保结束时间开始 
    @JsonProperty("mainten_end_date_before")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenEndDateBefore;
    
    
    // 本期维保结束时间结束 
    @JsonProperty("mainten_end_date_after")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenEndDateAfter;
    
    
    // 项目
	@JsonProperty("project")
    private String project;
    
	 
	// 软件名称
	@JsonProperty("software_name")
	private String softwareName; 
	 
	// 厂商
	private String company;
	  
 
    // 分类
    private String classify;
    
    
    // 本期维保开始时间开始
    @JsonProperty("mainten_begin_date_before")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenBeginDateBefore;
    
    
    // 本期维保开始时间结束
    @JsonProperty("mainten_begin_date_after")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenBeginDateAfter;
    
    // 联系人姓名
    @JsonProperty("user_name")
    private String userName;
    
    // 电话
    private String telephone;
    
    
    // 管理员
    private String admin;
     
   
}
