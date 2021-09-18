package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertDeviceTypeTop {

	//统计数量
	@JsonProperty("count")
    private Integer count;

	//资源池
    private String idcType;
    
    private String alertLevel;
    
    //设备类型
    private String deviceType;
    
    private Integer order;
    
    private String ip;
    
    private String companeyName;
    
    private String pod;
    
    private String roomId;
    
    private String month;
}
