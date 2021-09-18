package com.aspire.ums.cmdb.maintenance.payload;

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
    private Integer pageSize;
    // 每页数量
    private Integer pageNo;
    // 本期维保结束时间开始
    private Date maintenEndDateBefore;
    // 本期维保结束时间结束
    private Date maintenEndDateAfter;
    // 项目名称
    private String project;
    // 项目ID
    private String projectId;
	// 软件名称
	private String softwareName;
	// 厂商
	private String company;
    // 联系人姓名
    private String userName;
    // 电话
    private String telephone;
    // 管理员
    private String admin;
     
   
}
