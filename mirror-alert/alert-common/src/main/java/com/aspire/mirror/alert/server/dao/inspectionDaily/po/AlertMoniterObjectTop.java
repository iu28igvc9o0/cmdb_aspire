package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertMoniterObjectTop {

	//统计数量
	@JsonProperty("count")
    private Integer count;
	
	private Integer sum;
	//资源池
    private String idcType;
    
    private String alertLevel;
    
    //设备类型
    private String deviceType;
    
    private Integer order;
    
    private String moniterObject;
    
    private String rate;
    
    private String month;
    
    private String moniIndex;
}
