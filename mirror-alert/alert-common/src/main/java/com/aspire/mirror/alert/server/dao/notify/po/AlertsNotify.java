package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class AlertsNotify {
	
	
	/**
	 * ID
	 */
	private String id;
    /**
     * 告警ID
     */
    private String alertId;
    
    /**
     * 发件人ID
     */
    private String userId;
    
    /**
     * 发件人姓名
     */
    private String userName;
	
    /**
     * 通知类型, 0:短信,1:邮件
     */
    private String reportType;
    
    /**
     * 发件人邮箱
     */
    private String userEmail;
	
    /**
     * 短信/邮件 地址
     */
    private String destination;
    
    /**
     *   内容
     */
    private String message;
    
    /**
     * 发送状态
     * 0 失败，1 成功
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Date createTime;
      
   
    
    
}
