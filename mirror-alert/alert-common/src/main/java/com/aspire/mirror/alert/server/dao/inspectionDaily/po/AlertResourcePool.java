package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertResourcePool {

	//统计数量
	@JsonProperty("count")
    private Integer count;

	//资源池
    private String idcType;
    
    private Integer alertLevel;
    
    //设备类型
    private String deviceType;
    
  //设备类型
    private String month;
}
