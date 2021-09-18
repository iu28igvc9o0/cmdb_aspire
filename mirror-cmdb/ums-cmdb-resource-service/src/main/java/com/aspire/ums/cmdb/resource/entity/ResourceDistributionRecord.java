package com.aspire.ums.cmdb.resource.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResourceDistributionRecord {

    private int id;
    private int pre_distribute_id ; 
    private String device_id;
    private String device_ip;
    private String operate_content; 
    private String operate_id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operate_time; 
    private String remark; 
}
