package com.aspire.mirror.composite.service.cmdb.payload;

 

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CompMaintenSoftPageResp {
	
	 
        // id
		 private String id;
		
	    // 项目
	    private String project;
	    
	    // 分类
	    private String classify;
	    
	    // 软件名称
	    private String softwareName;
	    
	    // 单位
	    private String unit;
	    
	    // 数量
	    private String number;
	    
	    // 厂商
	    private String company;
	    
	    // 联系人姓名
	    private String userName;
	    
	    // 电话
	    private String telephone;
	    
	    // 本期维保开始时间
	    private Date maintenBeginDate;
	    
	    // 本期维保结束时间
	    private Date maintenEndDate;

	    // 管理员
	    private String admin;
	    
	    //备注 
	    private String remark ;

	

}
