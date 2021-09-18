package com.aspire.mirror.composite.payload.dashboard;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class ComAlertDeviceIpRequst  implements Serializable {
	
	private static final long serialVersionUID = 1L;
   //设备分类
    private String rs;
    
    // 设备类型 资源类型：1业务系统2资源池3机房4设备大类5设备小类6设备ip
    private Integer reType;
    
    private String item;
    private boolean multiQuery;//是否查询key下面的所有端口
    
    
    // 设备ip
    private List<String> ip;
	
}
