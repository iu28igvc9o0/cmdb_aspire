package com.aspire.ums.cmdb.deviceStatistic.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.ums.cmdb.InstanceRequest.payload
 * 类名称:    InstanceSearchRequest.java
 * 类描述:    资源查询接口类
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class InstanceSearchRequest {
    
	 
	// 分页页标
    private String pageSize;

    // 每页数量
    private String pageNo;
	
    //业务系统
    private String bizSystem;
   
	 // 资源池名称
	private String idcType;
    
    // 机房名称
	private String roomId;
    
   //设备分类
    private String deviceClass;
    
    // 设备类型
    private String deviceType;
    
    
    // 设备ip
    private String ip;
     

   
}
