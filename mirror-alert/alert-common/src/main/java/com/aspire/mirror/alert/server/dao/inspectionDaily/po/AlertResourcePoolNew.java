package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertResourcePoolNew {

	//统计数量
	@JsonProperty("count")
    private Integer count;

	//资源池
    private String idcType;
    
    private String alertLevel;
    
    //设备类型
    private String deviceType;
    
  //设备类型
    private String month;
    
    private Integer physicalMachineCount;

	private Integer firewallCount;

	private Integer routerCount;

	private Integer switchDeviceCount;

	private Integer cloudStorageCount;

	private Integer sdnControllerCount;

	private Integer diskArrayCount;

	private Integer tapeLibraryCount;
	
}
