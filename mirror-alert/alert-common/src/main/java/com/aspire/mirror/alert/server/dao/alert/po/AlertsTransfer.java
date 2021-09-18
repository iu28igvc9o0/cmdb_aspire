package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 告警转派
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    AlertsTransfer.java
 * 类描述:    告警转派po类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsTransfer {
	
	/**
	 * ID
	 */
	private String id;
    /**
     * 告警ID
     */
    private String alertId;
    
    /**
     * 操作人id
     */
    private String userId;
    
    /**
     * 操作人名称
     */
    private String userName;
    
    /**
     * 确认人id
     */
    private String confirmUserId;
    
    /**
     * 确认人名称
     */
    private String confirmUserName; 
     
    /**
     * 操作时间
     */
    private Date operationTime;

     
   
    
}
