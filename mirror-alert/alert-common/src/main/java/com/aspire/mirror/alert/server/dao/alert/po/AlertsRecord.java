package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 告警记录类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    Alerts.java
 * 类描述:    告警po类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsRecord {
	
	/**
	 * ID
	 */
	private String id;
    /**
     * 告警ID
     */
    private String alertId;
    
    /**
     * 操作人
     */
    private String userId;
    
    /**
     * 操作人名称
     */
    private String userName;
    
    /**
     * 操作类型   0-转派, 1-确认,2-派发工单, 3-清除,
     */
    private String operationType;
     
    /**
     * 操作时间
     */
    private Date operationTime;

    
    /**
     * 操作状态  0-失败 1-成功
     */
    private String operationStatus;
    
    /**
     * 操作内容
     */
    private String content;
    
      
    
}
